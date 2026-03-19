package com.yyj.codefarmcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyj.codefarmcommunity.entity.SysAuthRole;
import com.yyj.codefarmcommunity.entity.SysAuthRolePermission;
import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.yyj.codefarmcommunity.entity.SysAuthPermission;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import com.yyj.codefarmcommunity.service.SysAuthUserRoleService;
import com.yyj.codefarmcommunity.service.SysAuthPermissionService;
import com.yyj.codefarmcommunity.service.SysAuthRolePermissionService;
import com.yyj.codefarmcommunity.mapper.SysAuthRoleMapper;
import com.yyj.codefarmcommunity.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/*** @author 闫寅杰
* @description 针对表【sys_auth_role(角色表)】的数据库操作Service实现
* @createDate 2026-03-18 09:07:51
*/
@Service
public class SysAuthRoleServiceImpl extends ServiceImpl<SysAuthRoleMapper, SysAuthRole>
    implements SysAuthRoleService{

    private final SysAuthUserRoleService sysAuthUserRoleService;
    private final SysAuthRolePermissionService sysAuthRolePermissionService;
    private final SysAuthPermissionService sysAuthPermissionService;

    public SysAuthRoleServiceImpl(SysAuthUserRoleService sysAuthUserRoleService, SysAuthRolePermissionService sysAuthRolePermissionService, SysAuthPermissionService sysAuthPermissionService) {
        this.sysAuthUserRoleService = sysAuthUserRoleService;
        this.sysAuthRolePermissionService = sysAuthRolePermissionService;
        this.sysAuthPermissionService = sysAuthPermissionService;
    }

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

    @Override
    public List<SysAuthRole> getAllRoles() {
        return this.list(
            new QueryWrapper<SysAuthRole>()
                .eq("is_deleted", 0)
                .orderByDesc("id")
        );
    }

    @Override
    public SysAuthRole getRoleById(Long id) {
        return this.getOne(
            new QueryWrapper<SysAuthRole>()
                .eq("id", id)
                .eq("is_deleted", 0)
        );
    }

    @Override
    public List<SysAuthRole> getRolesByCondition(QueryWrapper<SysAuthRole> queryWrapper) {
        // 添加默认条件：未删除
        queryWrapper.eq("is_deleted", 0);
        return this.list(queryWrapper);
    }

    @Override
    public List<String> getPermissionsByRoleId(Long roleId) {
        List<String> permissions = new ArrayList<>();

        // 查询角色权限关联
        List<SysAuthRolePermission> rolePermissions = sysAuthRolePermissionService.list(
            new QueryWrapper<SysAuthRolePermission>()
                .eq("role_id", roleId)
                .eq("is_deleted", 0)
        );

        // 查询每个权限的详细信息
        for (SysAuthRolePermission rolePermission : rolePermissions) {
            SysAuthPermission permission = sysAuthPermissionService.getById(rolePermission.getPermissionId());
            if (permission != null && permission.getIsDeleted() == 0) {
                permissions.add(permission.getPermissionKey());
            }
        }

        // 去重
        return new ArrayList<>(new java.util.HashSet<>(permissions));
    }

    @Override
    public Map<String, List<String>> getAllRolesWithPermissions() {
        Map<String, List<String>> rolePermissionsMap = new HashMap<>();

        // 查询所有未删除的角色
        List<SysAuthRole> roles = this.getAllRoles();

        // 为每个角色查询权限
        for (SysAuthRole role : roles) {
            List<String> permissions = this.getPermissionsByRoleId(role.getId());
            rolePermissionsMap.put(role.getRoleKey(), permissions);
        }

        return rolePermissionsMap;
    }

    @Override
    public Map<String, List<String>> getRolesWithPermissionsByCondition(QueryWrapper<SysAuthRole> queryWrapper) {
        Map<String, List<String>> rolePermissionsMap = new HashMap<>();

        // 条件查询角色
        List<SysAuthRole> roles = this.getRolesByCondition(queryWrapper);

        // 为每个角色查询权限
        for (SysAuthRole role : roles) {
            List<String> permissions = this.getPermissionsByRoleId(role.getId());
            rolePermissionsMap.put(role.getRoleKey(), permissions);
        }

        return rolePermissionsMap;
    }

    @Override
    public boolean grantPermissions(Long roleId, List<Long> permissionIds) {
        // 获取角色现有的权限
        List<SysAuthRolePermission> existingPermissions = sysAuthRolePermissionService.list(
            new QueryWrapper<SysAuthRolePermission>()
                .eq("role_id", roleId)
                .eq("is_deleted", 0)
        );

        // 构建现有权限ID集合
        Set<Long> existingPermissionIds = new HashSet<>();
        for (SysAuthRolePermission perm : existingPermissions) {
            existingPermissionIds.add(perm.getPermissionId());
        }

        // 添加新的权限（避免重复）
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permissionId : permissionIds) {
                if (!existingPermissionIds.contains(permissionId)) {
                    SysAuthRolePermission rolePermission = new SysAuthRolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(permissionId);
                    rolePermission.setIsDeleted(0);
                    sysAuthRolePermissionService.save(rolePermission);
                }
            }
        }

        return true;
    }

    @Override
    public boolean revokePermissions(Long roleId, List<Long> permissionIds) {
        if (permissionIds != null && !permissionIds.isEmpty()) {
            sysAuthRolePermissionService.remove(
                new QueryWrapper<SysAuthRolePermission>()
                    .eq("role_id", roleId)
                    .in("permission_id", permissionIds)
            );
        }

        return true;
    }

    @Override
    public boolean deleteRole(Long id) {
        // 检查角色是否存在
        SysAuthRole role = this.getById(id);
        if (role == null || role.getIsDeleted() == 1) {
            throw new BusinessException(404, "角色不存在或已删除");
        }

        // 检查是否有用户拥有该角色
        List<SysAuthUserRole> userRoles = sysAuthUserRoleService.list(
            new QueryWrapper<SysAuthUserRole>()
                .eq("role_id", id)
                .eq("is_deleted", 0)
        );

        if (!userRoles.isEmpty()) {
            throw new BusinessException(400, "该角色已被用户使用，无法删除");
        }

        // 删除角色与权限的关联
        sysAuthRolePermissionService.remove(
            new QueryWrapper<SysAuthRolePermission>()
                .eq("role_id", id)
        );

        // 删除角色
        role.setIsDeleted(1);
        return this.updateById(role);
    }

    @Override
    public boolean deleteBatchRoles(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            for (Long id : ids) {
                // 循环调用单个删除接口
                this.deleteRole(id);
            }
        }

        return true;
    }

    @Override
    public long countRoles() {
        // 构建查询条件：未删除的角色
        QueryWrapper<SysAuthRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        
        // 执行统计
        return this.count(queryWrapper);
    }

}





