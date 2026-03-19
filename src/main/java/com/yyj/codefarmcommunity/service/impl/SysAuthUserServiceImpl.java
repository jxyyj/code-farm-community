package com.yyj.codefarmcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyj.codefarmcommunity.entity.SysAuthRole;
import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import com.yyj.codefarmcommunity.service.SysAuthUserRoleService;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import com.yyj.codefarmcommunity.service.SysAuthPermissionService;
import com.yyj.codefarmcommunity.mapper.SysAuthUserMapper;
import com.yyj.codefarmcommunity.vo.RegisterRequest;
import com.yyj.codefarmcommunity.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.HashSet;



/**
* @author 闫寅杰
* @description 针对表【sys_auth_user(用户信息表)】的数据库操作Service实现
* @createDate 2026-03-18 09:07:51
*/
@Service
public class SysAuthUserServiceImpl extends ServiceImpl<SysAuthUserMapper, SysAuthUser>
    implements SysAuthUserService{

    private final PasswordEncoder passwordEncoder;
    private final SysAuthUserRoleService sysAuthUserRoleService;
    private final SysAuthRoleService sysAuthRoleService;

    public SysAuthUserServiceImpl(PasswordEncoder passwordEncoder, SysAuthUserRoleService sysAuthUserRoleService, SysAuthRoleService sysAuthRoleService) {
        this.passwordEncoder = passwordEncoder;
        this.sysAuthUserRoleService = sysAuthUserRoleService;
        this.sysAuthRoleService = sysAuthRoleService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysAuthUser register(RegisterRequest registerRequest) {
        // 检查用户是否已存在
        SysAuthUser existingUser = this.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("user_name", registerRequest.getUserName())
        );

        if (existingUser != null) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 创建新用户
        SysAuthUser user = new SysAuthUser();
        user.setUserName(registerRequest.getUserName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setNickName(registerRequest.getNickName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setStatus(0); // 0 启用
        user.setIsDeleted(0); // 0 未删除

        // 保存用户到数据库
        this.save(user);

        // 新增用户默认分配 USER 角色
        SysAuthUserRole userRole = new SysAuthUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(sysAuthRoleService.list(
                new QueryWrapper<SysAuthRole>().eq("role_key", "USER"))
                .getFirst()
                .getId()
        );
        userRole.setIsDeleted(0); // 0 未删除
        sysAuthUserRoleService.save(userRole);

        return user;
    }

    @Override
    public SysAuthUser getUserById(Long id) {
        return this.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("id", id)
                .eq("is_deleted", 0)
        );
    }

    @Override
    public List<SysAuthUser> getAllUsers() {
        return this.list(
            new QueryWrapper<SysAuthUser>()
                .eq("is_deleted", 0)
                .orderByDesc("id")
        );
    }

    @Override
    public List<SysAuthUser> getUsersByCondition(QueryWrapper<SysAuthUser> queryWrapper) {
        // 添加默认条件：未删除
        queryWrapper.eq("is_deleted", 0);
        return this.list(queryWrapper);
    }

    @Override
    public IPage<SysAuthUser> listUsers(Integer page, Integer size) {
        // 校验参数
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1 || size > 100) {
            size = 10;
        }

        Page<SysAuthUser> userPage = new Page<>(page, size);
        QueryWrapper<SysAuthUser> queryWrapper = new QueryWrapper<>();

        // 添加默认条件：未删除
        queryWrapper.eq("is_deleted", 0);

        return this.page(userPage, queryWrapper);
    }

    @Override
    public IPage<SysAuthUser> searchUsers(Integer page, Integer size, String userName, Integer status) {
        // 校验参数
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1 || size > 100) {
            size = 10;
        }

        Page<SysAuthUser> userPage = new Page<>(page, size);
        QueryWrapper<SysAuthUser> queryWrapper = new QueryWrapper<>();

        if (userName != null && !userName.isEmpty()) {
            queryWrapper.like("user_name", userName);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        // 添加默认条件：未删除
        queryWrapper.eq("is_deleted", 0);

        return this.page(userPage, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysAuthUser user) {
        // 校验参数
        if (user == null) {
            throw new BusinessException(400, "用户信息不能为空");
        }
        if (user.getId() == null || user.getId() <= 0) {
            throw new BusinessException(400, "用户ID不能为空且必须大于0");
        }

        // 检查用户是否存在
        SysAuthUser existingUser = this.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("id", user.getId())
                .eq("is_deleted", 0)
        );

        if (existingUser == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 检查用户名是否已存在（排除当前用户）
        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            SysAuthUser duplicateUser = this.getOne(
                new QueryWrapper<SysAuthUser>()
                    .eq("user_name", user.getUserName())
                    .ne("id", user.getId())
                    .eq("is_deleted", 0)
            );

            if (duplicateUser != null) {
                throw new BusinessException(400, "用户名已存在");
            }
        }

        // 如果更新密码，需要加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        boolean success = this.updateById(user);
        if (!success) {
            throw new BusinessException(500, "更新用户失败");
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        // 校验参数
        if (id == null || id <= 0) {
            throw new BusinessException(400, "用户ID不能为空且必须大于0");
        }

        // 检查用户是否存在
        SysAuthUser user = this.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("id", id)
                .eq("is_deleted", 0)
        );

        if (user == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 删除用户角色关联
        sysAuthUserRoleService.remove(
            new QueryWrapper<SysAuthUserRole>()
                .eq("user_id", id)
        );

        // 删除用户（软删除）
        user.setIsDeleted(1);
        boolean success = this.updateById(user);
        if (!success) {
            throw new BusinessException(500, "删除用户失败");
        }

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchUsers(List<Long> ids) {
        // 校验参数
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(400, "用户ID列表不能为空");
        }

        for (Long id : ids) {
            // 循环调用单个删除接口
            this.deleteUser(id);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        // 校验参数
        if (userId == null || userId <= 0) {
            throw new BusinessException(400, "用户ID不能为空且必须大于0");
        }

        // 检查用户是否存在
        SysAuthUser user = this.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("id", userId)
                .eq("is_deleted", 0)
        );

        if (user == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 获取用户现有角色
        List<SysAuthUserRole> existingUserRoles = sysAuthUserRoleService.list(
            new QueryWrapper<SysAuthUserRole>()
                .eq("user_id", userId)
                .eq("is_deleted", 0)
        );

        // 构建现有角色ID集合
        Set<Long> existingRoleIds = new HashSet<>();
        for (SysAuthUserRole userRole : existingUserRoles) {
            existingRoleIds.add(userRole.getRoleId());
        }

        // 添加新的角色关联（避免重复）
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                if (!existingRoleIds.contains(roleId)) {
                    // 检查角色是否存在
                    SysAuthRole role = sysAuthRoleService.getOne(
                        new QueryWrapper<SysAuthRole>()
                            .eq("id", roleId)
                            .eq("is_deleted", 0)
                    );

                    if (role == null) {
                        throw new BusinessException(404, "角色不存在或已删除，角色ID：" + roleId);
                    }

                    SysAuthUserRole userRole = new SysAuthUserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    userRole.setIsDeleted(0);
                    sysAuthUserRoleService.save(userRole);
                }
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean revokeRoles(Long userId, List<Long> roleIds) {
        // 校验参数
        if (userId == null || userId <= 0) {
            throw new BusinessException(400, "用户ID不能为空且必须大于0");
        }

        // 检查用户是否存在
        SysAuthUser user = this.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("id", userId)
                .eq("is_deleted", 0)
        );

        if (user == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 回收角色
        if (roleIds != null && !roleIds.isEmpty()) {
            sysAuthUserRoleService.remove(
                new QueryWrapper<SysAuthUserRole>()
                    .eq("user_id", userId)
                    .in("role_id", roleIds)
            );
        }

        return true;
    }

    @Override
    public long countUsers() {
        // 构建查询条件：未删除的用户
        QueryWrapper<SysAuthUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        
        // 执行统计
        return this.count(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        // 校验参数
        if (userId == null || userId <= 0) {
            throw new BusinessException(400, "用户ID不能为空且必须大于0");
        }
        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new BusinessException(400, "旧密码不能为空");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new BusinessException(400, "新密码不能为空");
        }

        // 检查用户是否存在
        SysAuthUser user = this.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("id", userId)
                .eq("is_deleted", 0)
        );

        if (user == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(400, "旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        boolean success = this.updateById(user);
        if (!success) {
            throw new BusinessException(500, "修改密码失败");
        }

        return success;
    }

}




