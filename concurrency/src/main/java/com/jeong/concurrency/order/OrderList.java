package com.jeong.concurrency.order;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderList {

    /**
     * 구매 내역 일련번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 구매자명
     */
    private Integer orderNm;

    /**
     * 상품코드
     */
    @ManyToOne
    @JoinColumn(name="ProductCode")
    private Product product;

    /**
     * 구매 개수
     */
    private Integer Quit;
}
