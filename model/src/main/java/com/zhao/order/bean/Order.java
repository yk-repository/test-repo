package com.zhao.order.bean;

import com.zhao.product.bean.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Order {

    private Long userId;
    private Long orderId;
    private BigDecimal totalAmount;
    private String nickName;
    private String address;
    private List<Product> productList;

}
