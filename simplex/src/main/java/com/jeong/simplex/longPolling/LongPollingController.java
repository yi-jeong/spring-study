package com.jeong.simplex.longPolling;

import com.jeong.simplex.common.Like;
import com.jeong.simplex.common.LikeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class LongPollingController {

    private final LikeRepository likeRepository;
    private final LongPollingService longPollingService;

    public LongPollingController(LikeRepository likeRepository, LongPollingService longPollingService) {
        this.likeRepository = likeRepository;
        this.longPollingService = longPollingService;
    }

    @GetMapping("/long/like")
    public int getLikes(@RequestParam Integer likeSeq) throws InterruptedException, ExecutionException, TimeoutException {
        return longPollingService.checkLikes(likeSeq).get(60, TimeUnit.SECONDS);
    }

    @PostMapping("/long/like")
    public Integer like(@RequestParam Integer likeSeq) {
        Like optLike = likeRepository.findById(likeSeq).orElseThrow();
        optLike.setLikeCnt(optLike.getLikeCnt() + 1);
        Like saveLike = likeRepository.save(optLike);

        return saveLike.getLikeCnt();
    }
}
