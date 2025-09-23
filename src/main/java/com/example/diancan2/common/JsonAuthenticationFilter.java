package com.example.diancan2.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.diancan2.vo.ApiResponse;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        ApiResponse<String> apiResponse = ApiResponse.error("请先登录");
        
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        httpServletResponse.getWriter().write(jsonResponse);
        
        return false;
    }
}
