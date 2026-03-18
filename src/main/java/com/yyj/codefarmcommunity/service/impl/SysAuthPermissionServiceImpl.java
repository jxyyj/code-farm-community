package com.yyj.codefarmcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyj.codefarmcommunity.entity.SysAuthPermission;
import com.yyj.codefarmcommunity.entity.SysAuthRolePermission;
import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.yyj.codefarmcommunity.service.SysAuthPermissionService;
import com.yyj.codefarmcommunity.service.SysAuthRolePermissionService;
import com.yyj.codefarmcommunity.service.SysAuthUserRoleService;
import com.yyj.codefarmcommunity.mapper.SysAuthPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_permission(权限表)】的数据库操作Service实现
* @createDate 2026-03-18 09:07:51
*/
@Service
public class SysAuthPermissionServiceImpl extends ServiceImpl<SysAuthPermissionMapper, SysAuthPermission>
    implements SysAuthPermissionService{

    @Autowired
    private SysAuthUserRoleService sysAuthUserRoleService;

    @Autowired
    private SysAuthRolePermissionService sysAuthRolePermissionService;

    @Override
    public List<String> getPermissionsByUserId(Long userId) {
        List<String> permissions = new ArrayList<>();

        // 查询用户角色关联
        List<SysAuthUserRole> userRoles = sysAuthUserRoleService.list(
            new QueryWrapper<SysAuthUserRole>()
                .eq("user_id", userId)
                .eq("is_deleted", 0)
        );

        // 查询每个角色的权限
        for (SysAuthUserRole userRole : userRoles) {
            // 查询角色权限关联
            List<SysAuthRolePermission> rolePermissions = sysAuthRolePermissionService.list(
                new QueryWrapper<SysAuthRolePermission>()
                    .eq("role_id", userRole.getRoleId())
                    .eq("is_deleted", 0)
            );

            // 查询每个权限的详细信息
            for (SysAuthRolePermission rolePermission : rolePermissions) {
                SysAuthPermission permission = this.getById(rolePermission.getPermissionId());
                if (permission != null && permission.getIsDeleted() == 0) {
                    permissions.add(permission.getPermissionKey());
                }
            }
        }

        // 去重
        return new ArrayList<>(new java.util.HashSet<>(permissions));
    }

}





