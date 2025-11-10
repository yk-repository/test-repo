package com.zhao.order.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 全局异常处理
public class GlobalExceptionHandler {

    // 处理所有异常
   @ExceptionHandler(Throwable.class)
   public String error(Throwable e) {
      return "";
   }
}
