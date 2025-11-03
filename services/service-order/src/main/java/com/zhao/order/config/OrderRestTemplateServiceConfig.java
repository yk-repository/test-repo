package com.zhao.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// config 配置类, 用于配置 RestTemplate  bean, 并将其注册到 Spring 容器中,
// 以便在其他类中使用 @Autowired 注解进行注入, 并自动装配 RestTemplate  bean.
@Configuration
public class OrderRestTemplateServiceConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
