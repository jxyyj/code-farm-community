package com.yyj.codefarmcommunity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yyj.codefarmcommunity.entity.SysAuthRolePermission;
import com.yyj.codefarmcommunity.entity.dto.RoleWithPermission;

import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_role_permission(角色权限关联表)】的数据库操作Service
* @createDate 2026-03-18 09:07:51
*/
public interface SysAuthRolePermissionService extends IService<SysAuthRolePermission> {

    /**
     * 根据角色ID获取角色权限详情
     * @param roleId 角色ID
     * @return 角色权限详情
     */
    RoleWithPermission getRoleWithPermissions(Long roleId);

    /**
     * 分页查询角色权限列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    IPage<RoleWithPermission> listRoleWithPermissions(Integer page, Integer size);

    /**
     * 条件查询角色权限列表
     * @param page 页码
     * @param size 每页大小
     * @param roleName 角色名称
     * @param roleKey 角色唯一标识
     * @param permissionName 权限名称
     * @return 分页结果
     */
    IPage<RoleWithPermission> searchRoleWithPermissions(Integer page, Integer size, String roleName, String roleKey, String permissionName);

    /**
     * 保存角色权限关联
     * @param roleWithPermission 角色权限关联信息
     * @return 操作结果
     */
    boolean saveRolePermissions(RoleWithPermission roleWithPermission);

    /**
     * 更新角色权限关联
     * @param roleWithPermission 角色权限关联信息
     * @return 操作结果
     */
    boolean updateRolePermissions(RoleWithPermission roleWithPermission);

    /**
     * 删除角色权限关联
     * @param roleId 角色ID
     * @return 操作结果
     */
    boolean deleteRolePermissions(Long roleId);

    /**
     * 批量删除角色权限关联
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    boolean deleteBatchRolePermissions(List<Long> roleIds);
}

