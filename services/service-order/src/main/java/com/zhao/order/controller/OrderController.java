package com.zhao.order.controller;

import com.zhao.order.bean.Order;
import com.zhao.order.properties.OrderProperties;
import com.zhao.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RefreshScope // 激活配置属性的刷新功能
@RestController
@RequestMapping("/order")
public class OrderController {

    // 获取配置中心的内容
    // @Value("${order.timeout}")
    // private String timeout;

    // @Value("${order.auto-confirm}")
    // private String autoConfirm;

    @Autowired
    private OrderProperties orderProperties;

    @Autowired
    private OrderService orderService;

    // 创建订单
    @GetMapping("/create")
    public Order createOrder(@RequestParam("productId") Long productId,
                             @RequestParam("userId") Long userId) {
        return orderService.createOrder(productId, userId);
    }

    // 获取配置中心的配置文件
    @GetMapping("/config")
    public String getConfig() {
        // return "service-order.properties: timeout=" + timeout + ", autoConfirm=" + autoConfirm;
        return "service-order.properties: timeout=" + orderProperties.getTimeout() + ", autoConfirm="
                + orderProperties.getAutoConfirm() + ", dbUrl=" + orderProperties.getDbUrl();
    }

}
