package com.yyj.codefarmcommunity.config;

import com.yyj.codefarmcommunity.filter.JwtFilter;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT过滤器配置类
 */
@Configuration
public class JwtFilterConfig {

    private final SysAuthUserService sysAuthUserService;

    public JwtFilterConfig(SysAuthUserService sysAuthUserService) {
        this.sysAuthUserService = sysAuthUserService;
    }

    /**
     * JWT过滤器
     * @return JWT过滤器
     */
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(sysAuthUserService);
    }

}
