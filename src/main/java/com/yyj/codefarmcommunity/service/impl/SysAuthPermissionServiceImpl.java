package com.yyj.codefarmcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyj.codefarmcommunity.entity.SysAuthPermission;
import com.yyj.codefarmcommunity.entity.SysAuthRolePermission;
import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.yyj.codefarmcommunity.exception.BusinessException;
import com.yyj.codefarmcommunity.service.SysAuthPermissionService;
import com.yyj.codefarmcommunity.service.SysAuthRolePermissionService;
import com.yyj.codefarmcommunity.service.SysAuthUserRoleService;
import com.yyj.codefarmcommunity.mapper.SysAuthPermissionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_permission(权限表)】的数据库操作Service实现
* @createDate 2026-03-18 09:07:51
*/
@Service
public class SysAuthPermissionServiceImpl extends ServiceImpl<SysAuthPermissionMapper, SysAuthPermission>
    implements SysAuthPermissionService{
    private final Logger logger = LoggerFactory.getLogger(SysAuthPermissionServiceImpl.class);

    private final SysAuthUserRoleService sysAuthUserRoleService;
    private final SysAuthRolePermissionService sysAuthRolePermissionService;

    public SysAuthPermissionServiceImpl(SysAuthUserRoleService sysAuthUserRoleService, @org.springframework.context.annotation.Lazy SysAuthRolePermissionService sysAuthRolePermissionService) {
        this.sysAuthUserRoleService = sysAuthUserRoleService;
        this.sysAuthRolePermissionService = sysAuthRolePermissionService;
    }

    @Override
    public List<String> getPermissionsByUserId(Long userId) {
        List<String> permissions = new ArrayList<>();

        // 查询用户角色关联
        List<SysAuthUserRole> userRoles = sysAuthUserRoleService.list(
            new QueryWrapper<SysAuthUserRole>()
                .eq("user_id", userId)
                .eq("is_deleted", 0)
        );

        // 查询每个角色的权限
        for (SysAuthUserRole userRole : userRoles) {
            // 查询角色权限关联
            List<SysAuthRolePermission> rolePermissions = sysAuthRolePermissionService.list(
                new QueryWrapper<SysAuthRolePermission>()
                    .eq("role_id", userRole.getRoleId())
                    .eq("is_deleted", 0)
            );

            // 查询每个权限的详细信息
            for (SysAuthRolePermission rolePermission : rolePermissions) {
                SysAuthPermission permission = this.getById(rolePermission.getPermissionId());
                if (permission != null && permission.getIsDeleted() == 0) {
                    permissions.add(permission.getPermissionKey());
                }
            }
        }

        // 去重
        return new ArrayList<>(new java.util.HashSet<>(permissions));
    }

    @Override
    public List<SysAuthPermission> getAllPermissions() {
        return this.list(
            new QueryWrapper<SysAuthPermission>()
                .eq("is_deleted", 0)
                .orderByAsc("permission_key")
        );
    }

    @Override
    public SysAuthPermission getPermissionById(Long id) {
        return this.getOne(
            new QueryWrapper<SysAuthPermission>()
                .eq("id", id)
                .eq("is_deleted", 0)
        );
    }

    @Override
    public List<SysAuthPermission> getPermissionsByCondition(QueryWrapper<SysAuthPermission> queryWrapper) {
        // 添加默认条件：未删除
        queryWrapper.eq("is_deleted", 0);
        return this.list(queryWrapper);
    }

    @Override
    public IPage<SysAuthPermission> searchPermissions(Integer page, Integer size, String name, Integer type, Integer status, Long parentId) {
        Page<SysAuthPermission> permissionPage = new Page<>(page, size);
        QueryWrapper<SysAuthPermission> queryWrapper = new QueryWrapper<>();
        
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (parentId != null) {
            queryWrapper.eq("parent_id", parentId);
        }
        
        // 添加默认条件：未删除
        queryWrapper.eq("is_deleted", 0);
        
        return this.page(permissionPage, queryWrapper);
    }

    @Override
    public IPage<SysAuthPermission> listPermissions(Integer page, Integer size) {
        // 校验参数
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1 || size > 100) {
            size = 10;
        }
        
        // 记录日志
        logger.info("分页查询权限列表，页码：{}，每页大小：{}", page, size);
        
        Page<SysAuthPermission> permissionPage = new Page<>(page, size);
        QueryWrapper<SysAuthPermission> queryWrapper = new QueryWrapper<>();
        
        // 添加默认条件：未删除
        queryWrapper.eq("is_deleted", 0);
        
        return this.page(permissionPage, queryWrapper);
    }

    @Override
    public SysAuthPermission getPermissionByIdWithCheck(Long id) {
        // 校验参数
        if (id == null || id <= 0) {
            throw new BusinessException(400, "权限ID不能为空且必须大于0");
        }
        
        // 记录日志
        logger.info("根据ID查询权限，权限ID：{}", id);
        
        SysAuthPermission permission = this.getOne(
            new QueryWrapper<SysAuthPermission>()
                .eq("id", id)
                .eq("is_deleted", 0)
        );
        
        if (permission == null) {
            logger.warn("权限不存在或已删除，权限ID：{}", id);
            throw new BusinessException(404, "权限不存在或已删除");
        }
        
        return permission;
    }

    @Override
    public boolean addPermission(SysAuthPermission permission) {
        // 校验参数
        if (permission == null) {
            throw new BusinessException(400, "权限信息不能为空");
        }
        if (permission.getName() == null || permission.getName().isEmpty()) {
            throw new BusinessException(400, "权限名称不能为空");
        }
        if (permission.getPermissionKey() == null || permission.getPermissionKey().isEmpty()) {
            throw new BusinessException(400, "权限键不能为空");
        }
        
        // 记录日志
        logger.info("新增权限，权限名称：{}", permission.getName());
        
        // 检查权限键是否已存在
        SysAuthPermission existingPermission = this.getOne(
            new QueryWrapper<SysAuthPermission>()
                .eq("permission_key", permission.getPermissionKey())
                .eq("is_deleted", 0)
        );
        
        if (existingPermission != null) {
            logger.warn("权限键已存在，权限键：{}", permission.getPermissionKey());
            throw new BusinessException(400, "权限键已存在");
        }
        
        // 设置默认值
        permission.setIsDeleted(0);
        if (permission.getStatus() == null) {
            permission.setStatus(1);
        }
        if (permission.getOrderNum() == null) {
            permission.setOrderNum(0);
        }
        
        boolean success = this.save(permission);
        if (success) {
            logger.info("新增权限成功，权限ID：{}", permission.getId());
        } else {
            logger.error("新增权限失败，权限名称：{}", permission.getName());
            throw new BusinessException(500, "新增权限失败");
        }
        
        return success;
    }

    @Override
    public boolean updatePermission(SysAuthPermission permission) {
        // 校验参数
        if (permission == null) {
            throw new BusinessException(400, "权限信息不能为空");
        }
        if (permission.getId() == null || permission.getId() <= 0) {
            throw new BusinessException(400, "权限ID不能为空且必须大于0");
        }
        if (permission.getName() == null || permission.getName().isEmpty()) {
            throw new BusinessException(400, "权限名称不能为空");
        }
        if (permission.getPermissionKey() == null || permission.getPermissionKey().isEmpty()) {
            throw new BusinessException(400, "权限键不能为空");
        }
        
        // 记录日志
        logger.info("更新权限，权限ID：{}，权限名称：{}", permission.getId(), permission.getName());
        
        // 检查权限是否存在
        SysAuthPermission existingPermission = this.getOne(
            new QueryWrapper<SysAuthPermission>()
                .eq("id", permission.getId())
                .eq("is_deleted", 0)
        );
        
        if (existingPermission == null) {
            logger.warn("权限不存在或已删除，权限ID：{}", permission.getId());
            throw new BusinessException(404, "权限不存在或已删除");
        }
        
        // 检查权限键是否已存在（排除当前权限）
        SysAuthPermission duplicatePermission = this.getOne(
            new QueryWrapper<SysAuthPermission>()
                .eq("permission_key", permission.getPermissionKey())
                .ne("id", permission.getId())
                .eq("is_deleted", 0)
        );
        
        if (duplicatePermission != null) {
            logger.warn("权限键已存在，权限键：{}", permission.getPermissionKey());
            throw new BusinessException(400, "权限键已存在");
        }
        
        boolean success = this.updateById(permission);
        if (success) {
            logger.info("更新权限成功，权限ID：{}", permission.getId());
        } else {
            logger.error("更新权限失败，权限ID：{}", permission.getId());
            throw new BusinessException(500, "更新权限失败");
        }
        
        return success;
    }

    @Override
    public boolean deletePermission(Long id) {
        // 校验参数
        if (id == null || id <= 0) {
            throw new BusinessException(400, "权限ID不能为空且必须大于0");
        }
        
        // 记录日志
        logger.info("删除权限，权限ID：{}", id);
        
        // 检查权限是否存在
        SysAuthPermission permission = this.getOne(
            new QueryWrapper<SysAuthPermission>()
                .eq("id", id)
                .eq("is_deleted", 0)
        );
        
        if (permission == null) {
            logger.warn("权限不存在或已删除，权限ID：{}", id);
            throw new BusinessException(404, "权限不存在或已删除");
        }
        
        // 检查是否有角色关联该权限
        List<SysAuthRolePermission> rolePermissions = sysAuthRolePermissionService.list(
            new QueryWrapper<SysAuthRolePermission>()
                .eq("permission_id", id)
                .eq("is_deleted", 0)
        );
        
        if (!rolePermissions.isEmpty()) {
            logger.warn("该权限已被角色使用，无法删除，权限ID：{}", id);
            throw new BusinessException(400, "该权限已被角色使用，无法删除");
        }
        
        // 执行删除（软删除）
        permission.setIsDeleted(1);
        boolean success = this.updateById(permission);
        if (success) {
            logger.info("删除权限成功，权限ID：{}", id);
        } else {
            logger.error("删除权限失败，权限ID：{}", id);
            throw new BusinessException(500, "删除权限失败");
        }
        
        return success;
    }

    @Override
    public boolean deleteBatchPermissions(List<Long> ids) {
        // 校验参数
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(400, "权限ID列表不能为空");
        }
        
        // 记录日志
        logger.info("批量删除权限，权限ID列表：{}", ids);
        
        // 检查权限是否存在以及是否被角色使用
        for (Long id : ids) {
            if (id == null || id <= 0) {
                throw new BusinessException(400, "权限ID不能为空且必须大于0");
            }
            
            // 检查权限是否存在
            SysAuthPermission permission = this.getOne(
                new QueryWrapper<SysAuthPermission>()
                    .eq("id", id)
                    .eq("is_deleted", 0)
            );
            
            if (permission == null) {
                logger.warn("权限不存在或已删除，权限ID：{}", id);
                throw new BusinessException(404, "权限不存在或已删除");
            }
            
            // 检查是否有角色关联该权限
            List<SysAuthRolePermission> rolePermissions = sysAuthRolePermissionService.list(
                new QueryWrapper<SysAuthRolePermission>()
                    .eq("permission_id", id)
                    .eq("is_deleted", 0)
            );
            
            if (!rolePermissions.isEmpty()) {
                logger.warn("该权限已被角色使用，无法删除，权限ID：{}", id);
                throw new BusinessException(400, "该权限已被角色使用，无法删除");
            }
        }
        
        // 执行批量删除（软删除）
        for (Long id : ids) {
            SysAuthPermission permission = this.getById(id);
            permission.setIsDeleted(1);
            boolean success = this.updateById(permission);
            if (!success) {
                logger.error("删除权限失败，权限ID：{}", id);
                throw new BusinessException(500, "删除权限失败");
            }
        }
        
        logger.info("批量删除权限成功，删除数量：{}", ids.size());
        return true;
    }

    @Override
    public long countPermissions() {
        // 记录日志
        logger.info("统计权限总数");
        
        // 构建查询条件：未删除的权限
        QueryWrapper<SysAuthPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        
        // 执行统计
        long count = this.count(queryWrapper);
        
        logger.info("权限总数：{}", count);
        return count;
    }

}
