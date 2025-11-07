package com.zhao.order.config;

import feign.Logger;
import feign.RetryableException;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// config 配置类, 用于配置 RestTemplate  bean, 并将其注册到 Spring 容器中,
// 以便在其他类中使用 @Autowired 注解进行注入, 并自动装配 RestTemplate  bean.
@Configuration
public class OrderServiceConfig {

//    @LoadBalanced // 开启负载均衡( Ribbon )注解式, 用于在 RestTemplate 中开启负载均衡功能.当前方式使用后，其它方式会失效
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // 配置logger, 用于打印 RestTemplate 中的请求和响应日志
    @Bean
    public Logger.Level  feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    // 重试机制的配置，远程调用时，连接失败后多次重新连接
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(); // 使用默认重新连接的间隔时间
    }

}
