package com.jeong.concurrency.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

//    @PostMapping(value = "/order")
//    @ResponseBody
//    public Integer placeOrder(@RequestParam Integer memCode, @RequestParam String productName, @RequestParam int quantity) {
//        return orderService.placeOrder(memCode, productName, quantity);
//    }
}