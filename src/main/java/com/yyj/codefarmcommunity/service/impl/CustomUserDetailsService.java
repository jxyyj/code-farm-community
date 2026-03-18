package com.yyj.codefarmcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义用户详情服务
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final SysAuthUserService sysAuthUserService;
    private final SysAuthRoleService sysAuthRoleService;
    
    public CustomUserDetailsService(SysAuthUserService sysAuthUserService, 
                                   SysAuthRoleService sysAuthRoleService) {
        this.sysAuthUserService = sysAuthUserService;
        this.sysAuthRoleService = sysAuthRoleService;
    }
    
    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        // 从数据库中查询用户
        SysAuthUser user = sysAuthUserService.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("user_name", username)
        );
        
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        
        // 获取用户角色
        List<String> roles = sysAuthRoleService.getRolesByUserId(user.getId());
        
        // 构建用户详情
        return User.builder()
            .username(user.getUserName())
            .password(user.getPassword())
            .roles(roles.toArray(new String[0]))
            .build();
    }
}