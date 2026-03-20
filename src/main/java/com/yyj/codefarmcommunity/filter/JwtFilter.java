package com.yyj.codefarmcommunity.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyj.codefarmcommunity.common.Result;
import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import com.yyj.codefarmcommunity.utils.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT 过滤器
 */
@Slf4j
@Component
public class JwtFilter implements Filter {
    
    private final SysAuthUserService sysAuthUserService;
    private final ObjectMapper objectMapper;
    
    public JwtFilter(SysAuthUserService sysAuthUserService) {
        this.sysAuthUserService = sysAuthUserService;
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 获取请求路径
        String requestURI = httpRequest.getRequestURI();
        
        // 跳过登录、注册、测试和文档接口
        if (requestURI.equals("/api/login") || requestURI.equals("/api/register") ||
            requestURI.startsWith("/api/test/") ||
            requestURI.equals("/swagger-ui.html") || requestURI.startsWith("/swagger-ui/") ||
            requestURI.equals("/v3/api-docs") || requestURI.startsWith("/v3/api-docs/")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 获取请求头中的 token
        String token = null;
        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        
        // 如果没有 token，返回 401 未授权
        if (token == null || token.isEmpty()) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");
            Result result = Result.unauthorized("Unauthorized: No token provided. 请先登录");
            httpResponse.getWriter().write(objectMapper.writeValueAsString(result));
            log.error("JWT 过滤器 - 未提供 token 或 token 为空");
            return;
        }
        
        // 验证 token
        if (!JwtUtil.validateToken(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");
            Result result = Result.forbidden("Unauthorized: Invalid token. token已过期，请重新登录");
            httpResponse.getWriter().write(objectMapper.writeValueAsString(result));
            log.error("JWT 过滤器 - token 验证失败, token已过期, 请重新登录");
            return;
        }
        
        // 从 token 中获取用户信息
        Long userId = JwtUtil.parseToken(token).get("userId", Long.class);
        String username = JwtUtil.parseToken(token).get("username", String.class);
        
        // 从数据库中获取用户信息
        SysAuthUser user = sysAuthUserService.getUserById(userId);
        if (user == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");
            Result result = Result.notFound("Unauthorized: User not found. 用户不存在");
            httpResponse.getWriter().write(objectMapper.writeValueAsString(result));
            log.error("JWT 过滤器 - 从数据库中获取用户信息失败, userId: {}", userId);
            return;
        }

        // 从 token 中获取角色
        List<String> roles = new ArrayList<>();
        for (Object role : JwtUtil.parseToken(token).get("roles", List.class)) {
            if (role instanceof String) {
                roles.add((String) role);
            } else {
                log.warn("JWT 过滤器 - token 中角色格式错误, 角色: {}", role);
            }
        }

        // 构建用户详情
        UserDetails userDetails = User.builder()
            .username(user.getUserName())
            .password(user.getPassword())
            .roles(roles.toArray(new String[0]))
            .build();
        
        // 创建认证对象并设置到安全上下文
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 验证通过，继续处理请求
        chain.doFilter(request, response);
    }
    

}