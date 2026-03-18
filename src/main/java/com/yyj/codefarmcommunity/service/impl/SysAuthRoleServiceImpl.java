package com.yyj.codefarmcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyj.codefarmcommunity.entity.SysAuthRole;
import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import com.yyj.codefarmcommunity.service.SysAuthUserRoleService;
import com.yyj.codefarmcommunity.mapper.SysAuthRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_role(角色表)】的数据库操作Service实现
* @createDate 2026-03-18 09:07:51
*/
@Service
public class SysAuthRoleServiceImpl extends ServiceImpl<SysAuthRoleMapper, SysAuthRole>
    implements SysAuthRoleService{

    @Autowired
    private SysAuthUserRoleService sysAuthUserRoleService;

    @Override
    public List<String> getRolesByUserId(Long userId) {
        List<String> roles = new ArrayList<>();

        // 查询用户角色关联
        List<SysAuthUserRole> userRoles = sysAuthUserRoleService.list(
            new QueryWrapper<SysAuthUserRole>()
                .eq("user_id", userId)
                .eq("is_deleted", 0)
        );

        // 查询每个角色的详细信息
        for (SysAuthUserRole userRole : userRoles) {
            SysAuthRole role = this.getById(userRole.getRoleId());
            if (role != null && role.getIsDeleted() == 0) {
                roles.add(role.getRoleKey());
            }
        }

        // 如果用户没有角色，返回默认角色
        if (roles.isEmpty()) {
            roles.add("USER");
        }

        return roles;
    }

}





