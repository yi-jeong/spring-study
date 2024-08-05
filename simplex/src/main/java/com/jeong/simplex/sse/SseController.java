package com.jeong.simplex.sse;

import com.jeong.simplex.common.Like;
import com.jeong.simplex.common.LikeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class SseController {

    private final LikeRepository likeRepository;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseController(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @GetMapping("/sse/like")
    public SseEmitter getLikes() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

    @PostMapping("/sse/like")
    public Integer like(@RequestParam Integer likeSeq) {
        Like like = likeRepository.findById(likeSeq).orElseThrow();
        like.setLikeCnt(like.getLikeCnt() + 1);
        Like savedLike = likeRepository.save(like);
        return savedLike.getLikeCnt();
    }

    @Scheduled(fixedRate = 10000)
    private void sendLikeUpdates() {
        List<Like> likes = likeRepository.findAll();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(likes);
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}