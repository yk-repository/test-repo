package com.zhao.order.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

// 远程调用的拦截器
// 拦截器，在请求头中添加X-Token，用于远程调用时的认证,yml配置为配置，全部的远程调用都生效
@Component // 组件扫描，将该类注册到Spring容器中,
public class XTokenRequestInterceptor implements RequestInterceptor {
    /**
     * 请求拦截器
     * @param requestTemplate 请求模板
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("XTokenRequestInterceptor........启动");
        requestTemplate.header("X-Token", UUID.randomUUID().toString());
    }
}
