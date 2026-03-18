package com.yyj.codefarmcommunity.interceptor;

import com.yyj.codefarmcommunity.service.SysAuthUserService;
import com.yyj.codefarmcommunity.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    private final SysAuthUserService sysAuthUserService;
    
    public JwtInterceptor(SysAuthUserService sysAuthUserService) {
        this.sysAuthUserService = sysAuthUserService;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的 token
        String token = request.getHeader("Authorization");
        
        // 如果没有 token，返回 401 未授权
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: No token provided");
            return false;
        }
        
        // 验证 token
        if (!JwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid token");
            return false;
        }
        
        // 从 token 中获取用户信息
        Long userId = JwtUtil.getUserIdFromToken(token);
        String username = JwtUtil.getUsernameFromToken(token);
        
        // 从数据库中获取用户信息
        com.yyj.codefarmcommunity.entity.SysAuthUser user = sysAuthUserService.getById(userId);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: User not found");
            return false;
        }
        
        // 构建用户详情
        UserDetails userDetails = User.builder()
            .username(user.getUserName())
            .password(user.getPassword())
            .roles("USER") // 这里简化处理，实际应该从数据库中获取角色
            .build();
        
        // 创建认证对象并设置到安全上下文
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 验证通过，继续处理请求
        return true;
    }
}