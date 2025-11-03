package com.zhao.product.controller;

import com.zhao.product.bean.Product;
import com.zhao.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 查询商品
    @GetMapping("/get/{productId}")
    public Product getProduct(@PathVariable("productId") Long productId) {
        Product product = productService.getProductById(productId);
        System.out.println("查询商品：" + productId);
        return product;
    }
}
