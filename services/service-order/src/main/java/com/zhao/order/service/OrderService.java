package com.zhao.order.service;

import com.zhao.order.bean.Order;

public interface OrderService {
    Order createOrder(Long productId, Long userId);
}
