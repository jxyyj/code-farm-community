package com.yyj.codefarmcommunity.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yyj.codefarmcommunity.entity.SysAuthRole;
import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import com.yyj.codefarmcommunity.mapper.SysAuthUserMapper;
import com.yyj.codefarmcommunity.mapper.SysAuthUserRoleMapper;
import com.yyj.codefarmcommunity.mapper.SysAuthRoleMapper;
import com.yyj.codefarmcommunity.vo.RegisterRequest;
import com.yyj.codefarmcommunity.exception.BusinessException;
import org.springframework.security.core.Authentication;
import com.yyj.codefarmcommunity.utils.SecurityUtil;
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
public class SysAuthUserServiceImpl implements SysAuthUserService{

    private final PasswordEncoder passwordEncoder;
    private final SysAuthUserMapper sysAuthUserMapper;
    private final SysAuthUserRoleMapper sysAuthUserRoleMapper;
    private final SysAuthRoleMapper sysAuthRoleMapper;

    public SysAuthUserServiceImpl(PasswordEncoder passwordEncoder, SysAuthUserMapper sysAuthUserMapper, SysAuthUserRoleMapper sysAuthUserRoleMapper, SysAuthRoleMapper sysAuthRoleMapper) {
        this.passwordEncoder = passwordEncoder;
        this.sysAuthUserMapper = sysAuthUserMapper;
        this.sysAuthUserRoleMapper = sysAuthUserRoleMapper;
        this.sysAuthRoleMapper = sysAuthRoleMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysAuthUser register(RegisterRequest registerRequest) {
        // 检查用户是否已存在
        SysAuthUser existingUser = sysAuthUserMapper.selectByUserName(registerRequest.getUserName());

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
        sysAuthUserMapper.insert(user);

        // 新增用户默认分配 USER 角色
        SysAuthRole role = sysAuthRoleMapper.selectByRoleKey("USER");
        if (role == null) {
            throw new BusinessException(404, "USER角色不存在");
        }

        SysAuthUserRole userRole = new SysAuthUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());
        userRole.setIsDeleted(0); // 0 未删除
        sysAuthUserRoleMapper.insert(userRole);

        return user;
    }

    @Override
    public SysAuthUser getUserById(Long id) {
        return sysAuthUserMapper.selectById(id);
    }

    @Override
    public List<SysAuthUser> getAllUsers() {
        return sysAuthUserMapper.selectAll();
    }

    @Override
    public List<SysAuthUser> getUsersByCondition(String userName, Integer status) {
        return sysAuthUserMapper.selectByCondition(userName, status);
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

        int offset = (page - 1) * size;
        List<SysAuthUser> users = sysAuthUserMapper.selectPage(offset, size);
        long total = sysAuthUserMapper.count();

        Page<SysAuthUser> userPage = new Page<>(page, size);
        userPage.setRecords(users);
        userPage.setTotal(total);

        return userPage;
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

        int offset = (page - 1) * size;
        List<SysAuthUser> users = sysAuthUserMapper.searchPage(offset, size, userName, status);
        long total = sysAuthUserMapper.countByCondition(userName, status);

        Page<SysAuthUser> userPage = new Page<>(page, size);
        userPage.setRecords(users);
        userPage.setTotal(total);

        return userPage;
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
        SysAuthUser existingUser = sysAuthUserMapper.selectById(user.getId());

        if (existingUser == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 检查用户名是否已存在（排除当前用户）
        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
            SysAuthUser duplicateUser = sysAuthUserMapper.selectByUserName(user.getUserName());
            if (duplicateUser != null && !duplicateUser.getId().equals(user.getId())) {
                throw new BusinessException(400, "用户名已存在");
            }
        }

        // 如果更新密码，需要加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // 设置更新人
        user.setUpdateBy(SecurityUtil.getCurrentUserName());

        int success = sysAuthUserMapper.updateById(user);
        if (success == 0) {
            throw new BusinessException(500, "更新用户失败");
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        // 校验参数
        if (id == null || id <= 0) {
            throw new BusinessException(400, "用户ID不能为空且必须大于0");
        }

        // 检查用户是否存在
        SysAuthUser user = sysAuthUserMapper.selectById(id);

        if (user == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 删除用户角色关联
        sysAuthUserRoleMapper.deleteByUserId(id);

        // 删除用户（软删除）
        user.setIsDeleted(1);
        user.setUpdateBy(SecurityUtil.getCurrentUserName());
        int success = sysAuthUserMapper.updateById(user);
        if (success == 0) {
            throw new BusinessException(500, "删除用户失败");
        }

        return true;
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
        SysAuthUser user = sysAuthUserMapper.selectById(userId);

        if (user == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 获取用户现有角色
        List<SysAuthUserRole> existingUserRoles = sysAuthUserRoleMapper.selectByUserId(userId);

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
                    SysAuthRole role = sysAuthRoleMapper.selectById(roleId);

                    if (role == null) {
                        throw new BusinessException(404, "角色不存在或已删除，角色ID：" + roleId);
                    }

                    SysAuthUserRole userRole = new SysAuthUserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    userRole.setIsDeleted(0);
                    sysAuthUserRoleMapper.insert(userRole);
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
        SysAuthUser user = sysAuthUserMapper.selectById(userId);

        if (user == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 回收角色
        if (roleIds != null && !roleIds.isEmpty()) {
            sysAuthUserRoleMapper.deleteByUserIdAndRoleIds(userId, roleIds);
        }

        return true;
    }

    @Override
    public long countUsers() {
        return sysAuthUserMapper.count();
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
        SysAuthUser user = sysAuthUserMapper.selectById(userId);

        if (user == null) {
            throw new BusinessException(404, "用户不存在或已删除");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(400, "旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateBy(SecurityUtil.getCurrentUserName());
        int success = sysAuthUserMapper.updateById(user);
        if (success == 0) {
            throw new BusinessException(500, "修改密码失败");
        }

        return true;
    }

    @Override
    public SysAuthUser getUserByUserName(String userName) {
        return sysAuthUserMapper.selectByUserName(userName);
    }

    @Override
    public boolean save(SysAuthUser user) {
        user.setIsDeleted(0);
        return sysAuthUserMapper.insert(user) > 0;
    }

}




