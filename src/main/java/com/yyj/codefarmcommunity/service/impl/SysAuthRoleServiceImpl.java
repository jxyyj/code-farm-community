package com.yyj.codefarmcommunity.service.impl;

import com.yyj.codefarmcommunity.entity.SysAuthRole;
import com.yyj.codefarmcommunity.entity.SysAuthRolePermission;
import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.yyj.codefarmcommunity.entity.SysAuthPermission;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import com.yyj.codefarmcommunity.mapper.SysAuthRoleMapper;
import com.yyj.codefarmcommunity.mapper.SysAuthUserRoleMapper;
import com.yyj.codefarmcommunity.mapper.SysAuthRolePermissionMapper;
import com.yyj.codefarmcommunity.mapper.SysAuthPermissionMapper;
import com.yyj.codefarmcommunity.exception.BusinessException;
import com.yyj.codefarmcommunity.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class SysAuthRoleServiceImpl implements SysAuthRoleService{

    private final SysAuthRoleMapper sysAuthRoleMapper;
    private final SysAuthUserRoleMapper sysAuthUserRoleMapper;
    private final SysAuthRolePermissionMapper sysAuthRolePermissionMapper;
    private final SysAuthPermissionMapper sysAuthPermissionMapper;

    public SysAuthRoleServiceImpl(SysAuthRoleMapper sysAuthRoleMapper, SysAuthUserRoleMapper sysAuthUserRoleMapper, SysAuthRolePermissionMapper sysAuthRolePermissionMapper, SysAuthPermissionMapper sysAuthPermissionMapper) {
        this.sysAuthRoleMapper = sysAuthRoleMapper;
        this.sysAuthUserRoleMapper = sysAuthUserRoleMapper;
        this.sysAuthRolePermissionMapper = sysAuthRolePermissionMapper;
        this.sysAuthPermissionMapper = sysAuthPermissionMapper;
    }

    @Override
    public List<String> getRolesByUserId(Long userId) {
        List<String> roles = new ArrayList<>();

        // 查询用户角色关联
        List<SysAuthUserRole> userRoles = sysAuthUserRoleMapper.selectByUserId(userId);

        // 查询每个角色的详细信息
        for (SysAuthUserRole userRole : userRoles) {
            SysAuthRole role = sysAuthRoleMapper.selectById(userRole.getRoleId());
            if (role != null) {
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
        return sysAuthRoleMapper.selectAll();
    }

    @Override
    public SysAuthRole getRoleById(Long id) {
        return sysAuthRoleMapper.selectById(id);
    }

    @Override
    public List<SysAuthRole> getRolesByCondition(String roleName, Integer status) {
        return sysAuthRoleMapper.selectByCondition(roleName, status);
    }

    @Override
    public List<String> getPermissionsByRoleId(Long roleId) {
        List<String> permissions = new ArrayList<>();

        // 查询角色权限关联
        List<SysAuthRolePermission> rolePermissions = sysAuthRolePermissionMapper.selectByRoleId(roleId);

        // 查询每个权限的详细信息
        for (SysAuthRolePermission rolePermission : rolePermissions) {
            SysAuthPermission permission = sysAuthPermissionMapper.selectById(rolePermission.getPermissionId());
            if (permission != null) {
                permissions.add(permission.getPermissionKey());
            }
        }

        // 去重
        return new ArrayList<>(new HashSet<>(permissions));
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
    public Map<String, List<String>> getRolesWithPermissionsByCondition(String roleName, Integer status) {
        Map<String, List<String>> rolePermissionsMap = new HashMap<>();

        // 条件查询角色
        List<SysAuthRole> roles = this.getRolesByCondition(roleName, status);

        // 为每个角色查询权限
        for (SysAuthRole role : roles) {
            List<String> permissions = this.getPermissionsByRoleId(role.getId());
            rolePermissionsMap.put(role.getRoleKey(), permissions);
        }

        return rolePermissionsMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean grantPermissions(Long roleId, List<Long> permissionIds) {
        // 获取角色现有的权限
        List<SysAuthRolePermission> existingPermissions = sysAuthRolePermissionMapper.selectByRoleId(roleId);

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
                    sysAuthRolePermissionMapper.insert(rolePermission);
                }
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean revokePermissions(Long roleId, List<Long> permissionIds) {
        if (permissionIds != null && !permissionIds.isEmpty()) {
            sysAuthRolePermissionMapper.deleteByRoleIdAndPermissionIds(roleId, permissionIds);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long id) {
        // 检查角色是否存在
        SysAuthRole role = sysAuthRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException(404, "角色不存在或已删除");
        }

        // 检查是否有用户拥有该角色
        List<SysAuthUserRole> userRoles = sysAuthUserRoleMapper.selectByRoleId(id);

        if (!userRoles.isEmpty()) {
            throw new BusinessException(400, "该角色已被用户使用，无法删除");
        }

        // 删除角色与权限的关联
        sysAuthRolePermissionMapper.deleteByRoleId(id);

        // 删除角色
        role.setIsDeleted(1);
        role.setUpdateBy(SecurityUtil.getCurrentUserName());
        int success = sysAuthRoleMapper.updateById(role);
        return success > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        return sysAuthRoleMapper.count();
    }

    @Override
    public boolean save(SysAuthRole role) {
        role.setIsDeleted(0);
        return sysAuthRoleMapper.insert(role) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysAuthRole role) {
        role.setUpdateBy(SecurityUtil.getCurrentUserName());
        return sysAuthRoleMapper.updateById(role) > 0;
    }

}





