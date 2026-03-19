package com.yyj.codefarmcommunity.service;

import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_user_role(用户角色表)】的数据库操作Service
* @createDate 2026-03-18 09:07:51
*/
public interface SysAuthUserRoleService extends IService<SysAuthUserRole> {

    /**
     * 根据用户ID获取用户角色关联
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    List<SysAuthUserRole> getUserRolesByUserId(Long userId);

    /**
     * 根据角色ID获取用户角色关联
     * @param roleId 角色ID
     * @return 用户角色关联列表
     */
    List<SysAuthUserRole> getUserRolesByRoleId(Long roleId);

    /**
     * 条件查询用户角色关联
     * @param queryWrapper 查询条件
     * @return 用户角色关联列表
     */
    List<SysAuthUserRole> getUserRolesByCondition(QueryWrapper<SysAuthUserRole> queryWrapper);

    /**
     * 保存用户角色关联
     * @param userRole 用户角色关联信息
     * @return 操作结果
     */
    boolean saveUserRole(SysAuthUserRole userRole);

    /**
     * 更新用户角色关联
     * @param userRole 用户角色关联信息
     * @return 操作结果
     */
    boolean updateUserRole(SysAuthUserRole userRole);

    /**
     * 删除用户角色关联
     * @param id 用户角色关联ID
     * @return 操作结果
     */
    boolean deleteUserRole(Long id);

    /**
     * 批量删除用户角色关联
     * @param ids 用户角色关联ID列表
     * @return 操作结果
     */
    boolean deleteBatchUserRoles(List<Long> ids);

}

