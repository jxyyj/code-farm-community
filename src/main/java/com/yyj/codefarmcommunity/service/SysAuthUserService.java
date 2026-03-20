package com.yyj.codefarmcommunity.service;

import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.vo.RegisterRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_user(用户信息表)】的数据库操作Service
* @createDate 2026-03-18 09:07:51
*/
public interface SysAuthUserService {

    /**
     * 用户注册
     * @param registerRequest 注册请求
     * @return 注册后的用户信息
     */
    SysAuthUser register(RegisterRequest registerRequest);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    SysAuthUser getUserById(Long id);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<SysAuthUser> getAllUsers();

    /**
     * 条件查询用户
     * @param userName 用户名
     * @param status 状态
     * @return 用户列表
     */
    List<SysAuthUser> getUsersByCondition(String userName, Integer status);

    /**
     * 分页查询用户
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    IPage<SysAuthUser> listUsers(Integer page, Integer size);

    /**
     * 条件分页查询用户
     * @param page 页码
     * @param size 每页大小
     * @param userName 用户名
     * @param status 状态
     * @return 分页结果
     */
    IPage<SysAuthUser> searchUsers(Integer page, Integer size, String userName, Integer status);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 操作结果
     */
    boolean updateUser(SysAuthUser user);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 操作结果
     */
    boolean deleteUser(Long id);

    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 操作结果
     */
    boolean deleteBatchUsers(List<Long> ids);

    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    boolean assignRoles(Long userId, List<Long> roleIds);

    /**
     * 回收用户角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    boolean revokeRoles(Long userId, List<Long> roleIds);

    /**
     * 统计用户总数
     * @return 用户总数
     */
    long countUsers();

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 操作结果
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 根据用户名查询用户
     * @param userName 用户名
     * @return 用户信息
     */
    SysAuthUser getUserByUserName(String userName);

    /**
     * 保存用户
     * @param user 用户信息
     * @return 操作结果
     */
    boolean save(SysAuthUser user);

}

