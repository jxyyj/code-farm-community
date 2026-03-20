package com.yyj.codefarmcommunity.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {
    /**
     * 获取当前登录用户的用户名
     * @return 用户名
     */
    public static String getCurrentUserName() {
        // 1. 获取认证信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 2. 空校验 + 未登录校验（anonymousUser 是匿名用户标识）
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            return null; // 未登录返回null
        }

        // 3. 核心：从 getPrincipal() 中取用户名（源码可查）
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            // 标准情况：principal 是 UserDetails 实现类，调用 getUsername()
            return ((UserDetails) principal).getUsername();
        } else {
            // 特殊情况：principal 是字符串（如自定义认证），直接返回
            if (principal != null) {
                return principal.toString();
            }
            return "无法获取当前登录用户的用户名";
        }
    }
}