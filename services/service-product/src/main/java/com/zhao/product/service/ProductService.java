package com.zhao.product.service;

import com.zhao.product.bean.Product;

public interface ProductService {
    // 查询商品
    Product getProductById(Long productId);
}
