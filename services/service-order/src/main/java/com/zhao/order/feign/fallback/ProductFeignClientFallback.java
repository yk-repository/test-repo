package com.zhao.order.feign.fallback;

import com.zhao.order.feign.ProductFeignClient;
import com.zhao.product.bean.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 使用兜底回调类, 需要关闭 Retry（重试） 功能, 否则会导致无限循环调用
 */
@Component // 兜底回调类, 该注解将该类注册到 Spring 容器中
public class ProductFeignClientFallback implements ProductFeignClient {

    @Override
    public Product getProductById(Long productId) {
        System.out.println("兜底回调......");
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("兜底：未知商品" + productId);
        product.setPrice(new BigDecimal("99.9"));
        product.setNum(3);
        return product;
    }
}
