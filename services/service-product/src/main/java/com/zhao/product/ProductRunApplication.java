package com.zhao.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient // 开启服务注册与发现
@SpringBootApplication
public class ProductRunApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductRunApplication.class, args);
    }

}
