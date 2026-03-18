package com.yyj.codefarmcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyj.codefarmcommunity.entity.SysAuthRole;
import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import com.yyj.codefarmcommunity.service.SysAuthUserRoleService;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import com.yyj.codefarmcommunity.service.SysAuthPermissionService;
import com.yyj.codefarmcommunity.mapper.SysAuthUserMapper;
import com.yyj.codefarmcommunity.vo.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
* @author 闫寅杰
* @description 针对表【sys_auth_user(用户信息表)】的数据库操作Service实现
* @createDate 2026-03-18 09:07:51
*/
@Service
public class SysAuthUserServiceImpl extends ServiceImpl<SysAuthUserMapper, SysAuthUser>
    implements SysAuthUserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysAuthUserRoleService sysAuthUserRoleService;

    @Autowired
    private SysAuthRoleService sysAuthRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysAuthUser register(RegisterRequest registerRequest) {
        // 检查用户是否已存在
        SysAuthUser existingUser = this.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("user_name", registerRequest.getUserName())
        );

        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        SysAuthUser user = new SysAuthUser();
        user.setUserName(registerRequest.getUserName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setNickName(registerRequest.getNickName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setStatus(0); // 0 启用
        user.setIsDeleted(0); // 0 未删除

        // 保存用户到数据库
        this.save(user);

        // 新增用户默认分配 USER 角色
        SysAuthUserRole userRole = new SysAuthUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(sysAuthRoleService.list(
                new QueryWrapper<SysAuthRole>().eq("role_key", "USER"))
                .getFirst()
                .getId()
        );
        userRole.setIsDeleted(0); // 0 未删除
        sysAuthUserRoleService.save(userRole);

        return user;
    }

}




