package com.ljr.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljr.annotation.NoToken;
import com.ljr.common.R;
import com.ljr.common.ResultCode;
import com.ljr.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RequestMappingHandlerMapping handlerMapping;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            // 检查是否有NoToken注解
            HandlerMethod handlerMethod = (HandlerMethod) handlerMapping.getHandler(request).getHandler();
            if (handlerMethod != null && (handlerMethod.hasMethodAnnotation(NoToken.class) 
                    || handlerMethod.getBeanType().isAnnotationPresent(NoToken.class))) {
                // 有NoToken注解，直接放行
                filterChain.doFilter(request, response);
                return;
            }

            // 获取请求头中的Authorization
            String token = request.getHeader("Authorization");

            // 如果token存在且以"Bearer "开头
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);  // 去除 "Bearer " 部分

                // 验证Token
                if (jwtUtil.validateToken(token)) {
                    filterChain.doFilter(request, response);  // Token有效，继续处理请求
                    return;
                }
            }

            // 如果没有有效的Token，返回未授权错误
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(R.fail(ResultCode.UNAUTHORIZED)));
        } catch (Exception e) {
            log.error("Token验证发生错误", e);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(R.fail(ResultCode.SYSTEM_ERROR)));
        }
    }
}
