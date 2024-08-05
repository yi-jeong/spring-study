package com.jeong.simplex.polling;

import com.jeong.simplex.common.Like;
import com.jeong.simplex.common.LikeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PollingController {

    private final LikeRepository likeRepository;

    public PollingController(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @GetMapping("/poll/like")
    public int getLikes(@RequestParam Integer likeSeq) {
        Optional<Like> optLike = likeRepository.findById(likeSeq);
        return optLike.map(Like::getLikeCnt).orElse(0);
    }

    @PostMapping("/poll/like")
    public Integer like(@RequestParam Integer likeSeq) {
        Like optLike = likeRepository.findById(likeSeq).orElseThrow();
        optLike.setLikeCnt(optLike.getLikeCnt() + 1);
        Like saveLike = likeRepository.save(optLike);

        return saveLike.getLikeCnt();
    }
}