package com.zhao.product.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {

    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer num;

}
