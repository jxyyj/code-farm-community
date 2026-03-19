package com.yyj.codefarmcommunity.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yyj.codefarmcommunity.entity.SysAuthPermission;

import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_permission(权限表)】的数据库操作Service
* @createDate 2026-03-18 09:07:51
*/
public interface SysAuthPermissionService extends IService<SysAuthPermission> {

    /**
     * 根据用户ID获取权限
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getPermissionsByUserId(Long userId);

    /**
     * 查询所有权限
     * @return 权限列表
     */
    List<SysAuthPermission> getAllPermissions();

    /**
     * 根据ID查询权限
     * @param id 权限ID
     * @return 权限信息
     */
    SysAuthPermission getPermissionById(Long id);

    /**
     * 条件查询权限
     * @param queryWrapper 查询条件
     * @return 权限列表
     */
    List<SysAuthPermission> getPermissionsByCondition(QueryWrapper<SysAuthPermission> queryWrapper);

    /**
     * 分页条件查询权限
     * @param page 页码
     * @param size 每页大小
     * @param name 权限名称
     * @param type 权限类型
     * @param status 状态
     * @param parentId 父菜单ID
     * @return 分页结果
     */
    IPage<SysAuthPermission> searchPermissions(Integer page, Integer size, String name, Integer type, Integer status, Long parentId);

    /**
     * 分页查询权限列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    IPage<SysAuthPermission> listPermissions(Integer page, Integer size);

    /**
     * 根据ID查询权限
     * @param id 权限ID
     * @return 权限信息
     */
    SysAuthPermission getPermissionByIdWithCheck(Long id);

    /**
     * 新增权限
     * @param permission 权限信息
     * @return 操作结果
     */
    boolean addPermission(SysAuthPermission permission);

    /**
     * 更新权限
     * @param permission 权限信息
     * @return 操作结果
     */
    boolean updatePermission(SysAuthPermission permission);

    /**
     * 删除权限
     * @param id 权限ID
     * @return 操作结果
     */
    boolean deletePermission(Long id);

    /**
     * 批量删除权限
     * @param ids 权限ID列表
     * @return 操作结果
     */
    boolean deleteBatchPermissions(List<Long> ids);

    /**
     * 统计权限总数
     * @return 权限总数
     */
    long countPermissions();

}


