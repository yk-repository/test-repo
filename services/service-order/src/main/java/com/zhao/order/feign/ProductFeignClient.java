package com.zhao.order.feign;

import com.zhao.order.feign.fallback.ProductFeignClientFallback;
import com.zhao.product.bean.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 声明式实现远程调用服务中心
// fallback 是指定了一个 fallback 类，当调用失败时，会调用这个类的方法
@FeignClient(value = "service-product", contextId = "serviceProduct", fallback = ProductFeignClientFallback.class)
public interface ProductFeignClient {

    // mcv注解的两套使用逻辑
    // 1.标注在Controller上，是接受这样的请求
    // 2.标注在FigenClient上，是发送这样的请求
    @GetMapping("/api/product/get/{productId}")
    public Product getProductById(@PathVariable("productId") Long productId);
}
