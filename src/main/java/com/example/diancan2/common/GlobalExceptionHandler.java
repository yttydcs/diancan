package com.example.diancan2.common;

import com.example.diancan2.vo.ApiResponse;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    public ApiResponse<String> handleAuthorizationException(AuthorizationException e) {
        return ApiResponse.error("您没有权限执行此操作");
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleException(Exception e) {
        e.printStackTrace();
        return ApiResponse.error("服务器内部错误");
    }
}
