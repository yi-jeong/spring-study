package com.jeong.simplex.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeong.simplex.common.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<Integer, Comment> redisTemplate;
    private final Map<Integer, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SseEmitter createEmitter(Integer userId) {
        SseEmitter emitter = new SseEmitter();
        emitterMap.put(userId, emitter);

        emitter.onCompletion(() -> emitterMap.remove(userId));
        emitter.onTimeout(() -> emitterMap.remove(userId));
        emitter.onError((e) -> emitterMap.remove(userId));

        return emitter;
    }

    public void saveComment(Integer postId, Integer userId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("조회된 게시글이 없습니다."));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setPost(post);
        comment.setUser(user);
        comment.setComment(commentDto.getComment());

        Comment savedComment = commentRepository.save(comment);
        sendComment(post.getId(), savedComment);
    }

    public void sendComment(Integer userId, Comment comment){
        log.info("Comment {}", comment);

        SseEmitter emitter = emitterMap.get(userId);
        if(emitter != null){
            try {
                String commentJson = objectMapper.writeValueAsString(comment);
                emitter.send(SseEmitter.event().name("newComment").data(commentJson));
            }catch (Exception e){
                log.info(e.getMessage());
                emitterMap.remove(userId);
                throw new RuntimeException("댓글 전송에 실패했습니다.");
            }
        }
    }

}
