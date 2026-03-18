package com.yyj.codefarmcommunity.service.impl;

import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 自定义用户详情服务
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final SysAuthUserService sysAuthUserService;
    
    public CustomUserDetailsService(SysAuthUserService sysAuthUserService) {
        this.sysAuthUserService = sysAuthUserService;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中查询用户
        SysAuthUser user = sysAuthUserService.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("user_name", username)
        );
        
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        
        // 构建用户详情
        return User.builder()
            .username(user.getUserName())
            .password(user.getPassword())
            .roles("USER") // 这里简化处理，实际应该从数据库中获取角色
            .build();
    }
}