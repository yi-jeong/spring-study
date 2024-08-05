package com.jeong.simplex.longPolling;

import com.jeong.simplex.common.Like;
import com.jeong.simplex.common.LikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LongPollingService {
    private final LikeRepository likeRepository;

    public LongPollingService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Async
    public CompletableFuture<Integer> checkLikes(Integer likeSeq) throws InterruptedException {
        Integer previousLikeCount = null;

        Optional<Like> optLike = likeRepository.findById(likeSeq);
        if (optLike.isPresent()) {
            int currentLikeCount = optLike.get().getLikeCnt();
            if (previousLikeCount != null && currentLikeCount != previousLikeCount) {
                return CompletableFuture.completedFuture(currentLikeCount);
            }
            previousLikeCount = currentLikeCount;
        }

        TimeUnit.SECONDS.sleep(5);

        return CompletableFuture.completedFuture(previousLikeCount != null ? previousLikeCount : 0);
    }
}
