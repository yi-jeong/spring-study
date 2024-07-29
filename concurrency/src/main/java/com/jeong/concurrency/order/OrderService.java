package com.jeong.concurrency.order;

import com.jeong.concurrency.config.RedissonLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderListRepository orderListRepository;

    @Autowired
    public OrderService(ProductRepository productRepository, OrderListRepository orderListRepository) {
        this.productRepository = productRepository;
        this.orderListRepository = orderListRepository;
    }

    @Transactional
    public Integer placeOrder(Integer memCode, String productName, int quantity) {
        Product order = productRepository.findByProductName(productName);
        order.buyProduct(quantity);

        OrderList orderList = new OrderList();
        orderList.setOrderNm(memCode);
        orderList.setQuit(quantity);
        orderList.setProduct(order);
        orderListRepository.save(orderList);

        productRepository.saveAndFlush(order);

        return order.getQuantity();
    }

    @RedissonLock(key = "#productName")
    public Integer placeRedisOrder(Integer memCode, String productName, int quantity) {
        Product order = productRepository.findByProductName(productName);
        System.out.println("상품 검색 :" + order);
        order.buyProduct(quantity);

        OrderList orderList = new OrderList();
        orderList.setOrderNm(memCode);
        orderList.setQuit(quantity);
        orderList.setProduct(order);
        orderListRepository.save(orderList);

        System.out.println("상품 결과 :" + order + "," + order.getQuantity());

        productRepository.saveAndFlush(order);

        return order.getQuantity();
    }

    public Product getProduct(String productName){
        return productRepository.findByProductName(productName);
    }
}
