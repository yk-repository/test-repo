package com.zhao.product.service.impl;

import com.zhao.product.bean.Product;
import com.zhao.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {


    // 查询商品
    @Override
    public Product getProductById(Long productId) {
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("苹果" + productId);
        product.setPrice(new BigDecimal("99.9"));
        product.setNum(3);
        return product;
    }
}
