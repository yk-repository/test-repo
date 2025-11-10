package com.zhao.order.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhao.common.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

/**
 * 自定义阻塞异常
 * 捕获Web请求的阻塞异常
 */
@Component
public class MyBlockExceptionHandler implements BlockExceptionHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, String s,
                       BlockException e) throws Exception {
        // 设置响应编码
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(429);

        // 自定义返回结果
        PrintWriter writer = response.getWriter();
        R error = R.create(500, s + "被Sentinel限制了，原因是" + e.getClass(), null);
        String json = objectMapper.writeValueAsString(error);
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
