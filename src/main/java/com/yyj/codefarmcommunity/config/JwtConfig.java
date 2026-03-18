package com.yyj.codefarmcommunity.config;

import com.yyj.codefarmcommunity.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * JWT 配置类
 */
@Configuration
public class JwtConfig implements WebMvcConfigurer {
    
    private final JwtInterceptor jwtInterceptor;
    
    public JwtConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 JWT 拦截器
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有请求
                .addPathPatterns("/api/**")
                // 排除登录、注册等不需要 token 的接口
                .excludePathPatterns("/api/auth/login", "/api/auth/register", "/api/test/**");
    }
}