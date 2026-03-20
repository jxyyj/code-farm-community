package com.yyj.codefarmcommunity.service;

import com.yyj.codefarmcommunity.entity.SysAuthRole;

import java.util.List;
import java.util.Map;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_role(角色表)】的数据库操作Service
* @createDate 2026-03-18 09:07:51
*/
public interface SysAuthRoleService {

    /**
     * 根据用户ID查询角色
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
     * @param roleName 角色名称
     * @param status 状态
     * @return 角色列表
     */
    List<SysAuthRole> getRolesByCondition(String roleName, Integer status);

    /**
     * 根据角色ID查询权限
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<String> getPermissionsByRoleId(Long roleId);

    /**
     * 查询所有角色及其权限
     * @return 角色权限映射
     */
    Map<String, List<String>> getAllRolesWithPermissions();

    /**
     * 条件查询角色及其权限
     * @param roleName 角色名称
     * @param status 状态
     * @return 角色权限映射
     */
    Map<String, List<String>> getRolesWithPermissionsByCondition(String roleName, Integer status);

    /**
     * 为角色授予权限
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

    /**
     * 保存角色
     * @param role 角色信息
     * @return 操作结果
     */
    boolean save(SysAuthRole role);

    /**
     * 更新角色
     * @param role 角色信息
     * @return 操作结果
     */
    boolean updateById(SysAuthRole role);

}  


