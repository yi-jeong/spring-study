package com.jeong.concurrency.order;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String productName;
    private Integer quantity;

    // 구매
    public void buyProduct(Integer buyCnt){
        if ((quantity - buyCnt) < 0) {
            throw new IllegalArgumentException();
        }
        quantity -= buyCnt;
    }
}
