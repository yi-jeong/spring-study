package com.jeong.concurrency;

import com.jeong.concurrency.order.OrderService;
import com.jeong.concurrency.order.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderTest {
    @Autowired
    private OrderService orderService;

    private static final int THREAD_COUNT = 19;

    @BeforeEach
    public void setUp() {
    }

    @Test
    void testConcurrentOrders() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            Integer id = i;
            final int orderQuantity = 1;
            executor.submit(() -> {
                try {
                    Integer cnt = orderService.placeOrder(id, "test", orderQuantity);
                    System.out.println("상품 구매 수 = " + cnt);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product after = orderService.getProduct("test");

        assertThat(after.getQuantity()).isZero();
        System.out.println("잔여 상품 수 = " + after.getQuantity());
    }

    @Test
    void testConcurrentRedisOrders() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            Integer id = i;
            final int orderQuantity = 1;
            executor.submit(() -> {
                try {
                    Integer cnt = orderService.placeRedisOrder(id, "test", orderQuantity);
                    System.out.println("상품 구매 수 = " + cnt);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product after = orderService.getProduct("test");

        assertThat(after.getQuantity()).isZero();
        System.out.println("잔여 상품 수 = " + after.getQuantity());
    }
}
