package com.zhao.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// 全局过滤器，用于记录请求耗时
@Component
@Slf4j
public class RtGlobalFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求数据
        ServerHttpRequest request = exchange.getRequest();
        // 获取响应数据
        ServerHttpResponse response = exchange.getResponse();
        // 获取请求路径
        String uri = request.getURI().toString();
        // 获取请求开始时间
        long startTime = System.currentTimeMillis();
        log.info("请求【{}】开始，时间：{}", uri,startTime);
        // ================== 以上是前置逻辑 ==================
        // 放行请求
        Mono<Void> filter = chain.filter(exchange);
        filter.doFinally(signalType -> {
            // ================== 以下是后置逻辑 ==================
            long endTime = System.currentTimeMillis();
            log.info("请求【{}】结束，时间：{}，耗时：{}ms", uri, endTime, endTime - startTime);
        });

        return filter;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
