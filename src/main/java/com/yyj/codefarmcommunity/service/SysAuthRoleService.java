package com.yyj.codefarmcommunity.service;

import com.yyj.codefarmcommunity.entity.SysAuthRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;
import java.util.Map;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_role(角色表)】的数据库操作Service
* @createDate 2026-03-18 09:07:51
*/
public interface SysAuthRoleService extends IService<SysAuthRole> {

    /**
     * 根据用户ID获取角色
     * @param userId 用户ID
     * @return 角色列表
     */
    List<String> getRolesByUserId(Long userId);

    /**
     * 查询所有角色
     * @return 角色列表
     */
    List<SysAuthRole> getAllRoles();

    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色信息
     */
    SysAuthRole getRoleById(Long id);

    /**
     * 条件查询角色
     * @param queryWrapper 查询条件
     * @return 角色列表
     */
    List<SysAuthRole> getRolesByCondition(QueryWrapper<SysAuthRole> queryWrapper);

    /**
     * 查询角色的所有权限
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<String> getPermissionsByRoleId(Long roleId);

    /**
     * 查询所有角色的所有权限
     * @return 角色权限映射
     */
    Map<String, List<String>> getAllRolesWithPermissions();

    /**
     * 条件查询所有角色的所有权限
     * @param queryWrapper 查询条件
     * @return 角色权限映射
     */
    Map<String, List<String>> getRolesWithPermissionsByCondition(QueryWrapper<SysAuthRole> queryWrapper);

    /**
     * 为角色授权
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 操作结果
     */
    boolean grantPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 回收角色权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 操作结果
     */
    boolean revokePermissions(Long roleId, List<Long> permissionIds);

    /**
     * 删除角色
     * @param id 角色ID
     * @return 操作结果
     */
    boolean deleteRole(Long id);

    /**
     * 批量删除角色
     * @param ids 角色ID列表
     * @return 操作结果
     */
    boolean deleteBatchRoles(List<Long> ids);

    /**
     * 统计角色总数
     * @return 角色总数
     */
    long countRoles();

}


