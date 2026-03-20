package com.yyj.codefarmcommunity.service.impl;

import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.yyj.codefarmcommunity.exception.BusinessException;
import com.yyj.codefarmcommunity.service.SysAuthUserRoleService;
import com.yyj.codefarmcommunity.mapper.SysAuthUserRoleMapper;
import com.yyj.codefarmcommunity.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_user_role(用户角色表)】的数据库操作Service实现
* @createDate 2026-03-18 09:07:51
*/
@Service
public class SysAuthUserRoleServiceImpl implements SysAuthUserRoleService{

    private final SysAuthUserRoleMapper sysAuthUserRoleMapper;

    public SysAuthUserRoleServiceImpl(SysAuthUserRoleMapper sysAuthUserRoleMapper) {
        this.sysAuthUserRoleMapper = sysAuthUserRoleMapper;
    }

    @Override
    public List<SysAuthUserRole> getUserRolesByUserId(Long userId) {
        return sysAuthUserRoleMapper.selectByUserId(userId);
    }

    @Override
    public List<SysAuthUserRole> getUserRolesByRoleId(Long roleId) {
        return sysAuthUserRoleMapper.selectByRoleId(roleId);
    }

    @Override
    public List<SysAuthUserRole> getUserRolesByCondition(Long userId, Long roleId) {
        if (userId != null) {
            return sysAuthUserRoleMapper.selectByUserId(userId);
        } else if (roleId != null) {
            return sysAuthUserRoleMapper.selectByRoleId(roleId);
        } else {
            return List.of();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserRole(SysAuthUserRole userRole) {
        // 校验参数
        if (userRole == null) {
            throw new BusinessException(400, "用户角色关联信息不能为空");
        }
        if (userRole.getUserId() == null || userRole.getUserId() <= 0) {
            throw new BusinessException(400, "用户ID不能为空且必须大于0");
        }
        if (userRole.getRoleId() == null || userRole.getRoleId() <= 0) {
            throw new BusinessException(400, "角色ID不能为空且必须大于0");
        }

        // 检查是否已存在
        SysAuthUserRole existingUserRole = sysAuthUserRoleMapper.selectByUserIdAndRoleId(userRole.getUserId(), userRole.getRoleId());

        if (existingUserRole != null) {
            throw new BusinessException(400, "用户角色关联已存在");
        }

        // 设置默认值
        userRole.setIsDeleted(0);
        // 设置创建人和更新人
        String currentUserName = SecurityUtil.getCurrentUserName();
        userRole.setCreatedBy(currentUserName);
        userRole.setUpdateBy(currentUserName);

        int success = sysAuthUserRoleMapper.insert(userRole);
        if (success == 0) {
            throw new BusinessException(500, "保存用户角色关联失败");
        }

        return success > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserRole(SysAuthUserRole userRole) {
        // 校验参数
        if (userRole == null) {
            throw new BusinessException(400, "用户角色关联信息不能为空");
        }
        if (userRole.getId() == null || userRole.getId() <= 0) {
            throw new BusinessException(400, "用户角色关联ID不能为空且必须大于0");
        }

        // 检查是否存在
        SysAuthUserRole existingUserRole = sysAuthUserRoleMapper.selectById(userRole.getId());

        if (existingUserRole == null) {
            throw new BusinessException(404, "用户角色关联不存在或已删除");
        }

        // 设置更新人
        userRole.setUpdateBy(SecurityUtil.getCurrentUserName());
        int success = sysAuthUserRoleMapper.updateById(userRole);
        if (success == 0) {
            throw new BusinessException(500, "更新用户角色关联失败");
        }

        return success > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserRole(Long id) {
        // 校验参数
        if (id == null || id <= 0) {
            throw new BusinessException(400, "用户角色关联ID不能为空且必须大于0");
        }

        // 检查是否存在
        SysAuthUserRole userRole = sysAuthUserRoleMapper.selectById(id);

        if (userRole == null) {
            throw new BusinessException(404, "用户角色关联不存在或已删除");
        }

        // 软删除
        userRole.setIsDeleted(1);
        userRole.setUpdateBy(SecurityUtil.getCurrentUserName());
        int success = sysAuthUserRoleMapper.updateById(userRole);
        if (success == 0) {
            throw new BusinessException(500, "删除用户角色关联失败");
        }

        return success > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatchUserRoles(List<Long> ids) {
        // 校验参数
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(400, "用户角色关联ID列表不能为空");
        }

        for (Long id : ids) {
            // 循环调用单个删除接口
            this.deleteUserRole(id);
        }

        return true;
    }

}





