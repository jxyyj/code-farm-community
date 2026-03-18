package com.yyj.codefarmcommunity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.yyj.codefarmcommunity.filter.JwtFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * 密码编码器
     * @return 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 认证管理器
     * @param config 认证配置
     * @return 认证管理器
     * @throws Exception 异常
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    

    /**
     * 配置安全规则
     * @param http HttpSecurity
     * @param jwtFilter JWT过滤器
     * @return SecurityFilterChain
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/login", "/api/register", "/api/test/**",
                                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll() // 允许未认证的请求访问登录、注册、测试和文档接口
                .requestMatchers("/api/me").authenticated() // 获取当前用户信息需要认证
                .requestMatchers("/api/user/get/{id}", "/api/user/update", "/api/user/password").hasAnyRole("USER", "ADMIN") // 用户可以查看和修改自己的信息, 可以修改自己的密码
                .anyRequest().hasRole("ADMIN") // 其他请求需要认证
            )
            .httpBasic(AbstractHttpConfigurer::disable) // 禁用 HTTP Basic 认证
            .formLogin(AbstractHttpConfigurer::disable); // 禁用表单登录
        
        // 添加JWT过滤器，在用户名密码认证过滤器之前执行
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}