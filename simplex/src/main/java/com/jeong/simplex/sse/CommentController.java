package com.jeong.simplex.sse;

import com.jeong.simplex.common.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public String createComment(@PathVariable Integer postId, @RequestBody CommentDto commentDto) {
        commentService.saveComment(postId, 1, commentDto);

        return "댓글이 등록 되었습니다.";
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam(value = "userId") Integer userId) {
        return commentService.createEmitter(userId);
    }

}