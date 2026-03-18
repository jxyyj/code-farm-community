package com.yyj.codefarmcommunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置类
 */
@Configuration
public class CorsConfig {
    
    /**
     * 配置CORS过滤器
     * @return CORS过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建CORS配置对象
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许所有来源
        config.addAllowedOrigin("*");
        // 允许所有方法
        config.addAllowedMethod("*");
        // 允许所有头部
        config.addAllowedHeader("*");
        // 允许携带凭证
        config.setAllowCredentials(true);
        // 预检请求的有效期，单位秒
        config.setMaxAge(3600L);
        
        // 创建CORS配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径应用CORS配置
        source.registerCorsConfiguration("/**", config);
        
        // 创建并返回CORS过滤器
        return new CorsFilter(source);
    }
}
