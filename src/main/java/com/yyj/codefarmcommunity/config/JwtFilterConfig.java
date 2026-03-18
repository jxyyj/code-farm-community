package com.yyj.codefarmcommunity.config;

import com.yyj.codefarmcommunity.filter.JwtFilter;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT过滤器配置类
 */
@Configuration
public class JwtFilterConfig {

    private final SysAuthUserService sysAuthUserService;
    private final SysAuthRoleService sysAuthRoleService;

    public JwtFilterConfig(SysAuthUserService sysAuthUserService, 
                          SysAuthRoleService sysAuthRoleService) {
        this.sysAuthUserService = sysAuthUserService;
        this.sysAuthRoleService = sysAuthRoleService;
    }

    /**
     * JWT过滤器
     * @return JWT过滤器
     */
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(sysAuthUserService, sysAuthRoleService);
    }

}
