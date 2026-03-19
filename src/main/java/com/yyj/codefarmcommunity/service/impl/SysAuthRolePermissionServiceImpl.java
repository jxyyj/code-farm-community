package com.yyj.codefarmcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yyj.codefarmcommunity.entity.SysAuthPermission;
import com.yyj.codefarmcommunity.entity.SysAuthRole;
import com.yyj.codefarmcommunity.entity.SysAuthRolePermission;
import com.yyj.codefarmcommunity.entity.dto.RoleWithPermission;
import com.yyj.codefarmcommunity.exception.BusinessException;
import com.yyj.codefarmcommunity.service.SysAuthPermissionService;
import com.yyj.codefarmcommunity.service.SysAuthRolePermissionService;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import com.yyj.codefarmcommunity.mapper.SysAuthRolePermissionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_role_permission(角色权限关联表)】的数据库操作Service实现
* @createDate 2026-03-18 09:07:51
*/
@Service
public class SysAuthRolePermissionServiceImpl extends ServiceImpl<SysAuthRolePermissionMapper, SysAuthRolePermission>
    implements SysAuthRolePermissionService{

    private final Logger logger = LoggerFactory.getLogger(SysAuthRolePermissionServiceImpl.class);

    private final SysAuthRoleService sysAuthRoleService;
    private final SysAuthPermissionService sysAuthPermissionService;

    public SysAuthRolePermissionServiceImpl(@Lazy SysAuthRoleService sysAuthRoleService, SysAuthPermissionService sysAuthPermissionService) {
        this.sysAuthRoleService = sysAuthRoleService;
        this.sysAuthPermissionService = sysAuthPermissionService;
    }

    @Override
    public RoleWithPermission getRoleWithPermissions(Long roleId) {
        // 校验参数
        if (roleId == null || roleId <= 0) {
            throw new BusinessException(400, "角色ID不能为空且必须大于0");
        }

        // 记录日志
        logger.info("根据角色ID获取角色权限详情，角色ID：{}", roleId);

        // 查询角色信息
        SysAuthRole role = sysAuthRoleService.getRoleById(roleId);
        if (role == null) {
            logger.warn("角色不存在或已删除，角色ID：{}", roleId);
            throw new BusinessException(404, "角色不存在或已删除");
        }

        // 查询角色关联的权限
        List<SysAuthRolePermission> rolePermissions = this.list(
            new QueryWrapper<SysAuthRolePermission>()
                .eq("role_id", roleId)
                .eq("is_deleted", 0)
        );

        // 查询权限详情
        List<SysAuthPermission> permissions = new ArrayList<>();
        for (SysAuthRolePermission rolePermission : rolePermissions) {
            SysAuthPermission permission = sysAuthPermissionService.getPermissionById(rolePermission.getPermissionId());
            if (permission != null) {
                permissions.add(permission);
            }
        }

        // 构建返回结果
        RoleWithPermission roleWithPermission = new RoleWithPermission();
        // 复制角色信息
        roleWithPermission.setId(role.getId());
        roleWithPermission.setRoleName(role.getRoleName());
        roleWithPermission.setRoleKey(role.getRoleKey());
        roleWithPermission.setIsDeleted(role.getIsDeleted());
        roleWithPermission.setCreatedBy(role.getCreatedBy());
        roleWithPermission.setCreatedTime(role.getCreatedTime());
        roleWithPermission.setUpdateBy(role.getUpdateBy());
        roleWithPermission.setUpdateTime(role.getUpdateTime());
        // 设置权限列表
        roleWithPermission.setPermissions(permissions);

        return roleWithPermission;
    }

    @Override
    public IPage<RoleWithPermission> listRoleWithPermissions(Integer page, Integer size) {
        // 校验参数
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1 || size > 100) {
            size = 10;
        }

        // 记录日志
        logger.info("分页查询角色权限列表，页码：{}，每页大小：{}", page, size);

        // 查询所有角色
        List<SysAuthRole> roles = sysAuthRoleService.getAllRoles();

        // 构建角色权限列表
        List<RoleWithPermission> roleWithPermissions = new ArrayList<>();
        for (SysAuthRole role : roles) {
            RoleWithPermission roleWithPermission = getRoleWithPermissions(role.getId());
            roleWithPermissions.add(roleWithPermission);
        }

        // 分页处理
        Page<RoleWithPermission> roleWithPermissionPage = new Page<>(page, size);
        int start = (int) ((page - 1) * size);
        int end = Math.min(start + size, roleWithPermissions.size());
        if (start < roleWithPermissions.size()) {
            roleWithPermissionPage.setRecords(roleWithPermissions.subList(start, end));
        }
        roleWithPermissionPage.setTotal(roleWithPermissions.size());

        return roleWithPermissionPage;
    }

    @Override
    public IPage<RoleWithPermission> searchRoleWithPermissions(Integer page, Integer size, String roleName, String roleKey, String permissionName) {
        // 校验参数
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1 || size > 100) {
            size = 10;
        }

        // 记录日志
        logger.info("条件查询角色权限列表，页码：{}，每页大小：{}，角色名称：{}，角色键：{}，权限名称：{}", page, size, roleName, roleKey, permissionName);

        // 构建查询条件
        QueryWrapper<SysAuthRole> queryWrapper = new QueryWrapper<>();
        if (roleName != null && !roleName.isEmpty()) {
            queryWrapper.like("role_name", roleName);
        }
        if (roleKey != null && !roleKey.isEmpty()) {
            queryWrapper.like("role_key", roleKey);
        }

        // 查询符合条件的角色
        List<SysAuthRole> roles = sysAuthRoleService.getRolesByCondition(queryWrapper);

        // 构建角色权限列表
        List<RoleWithPermission> roleWithPermissions = new ArrayList<>();
        for (SysAuthRole role : roles) {
            RoleWithPermission roleWithPermission = getRoleWithPermissions(role.getId());
            // 过滤权限名称
            if (permissionName != null && !permissionName.isEmpty()) {
                List<SysAuthPermission> filteredPermissions = new ArrayList<>();
                for (SysAuthPermission permission : roleWithPermission.getPermissions()) {
                    if (permission.getName().contains(permissionName)) {
                        filteredPermissions.add(permission);
                    }
                }
                if (!filteredPermissions.isEmpty()) {
                    roleWithPermission.setPermissions(filteredPermissions);
                    roleWithPermissions.add(roleWithPermission);
                }
            } else {
                roleWithPermissions.add(roleWithPermission);
            }
        }

        // 分页处理
        Page<RoleWithPermission> roleWithPermissionPage = new Page<>(page, size);
        int start = (int) ((page - 1) * size);
        int end = Math.min(start + size, roleWithPermissions.size());
        if (start < roleWithPermissions.size()) {
            roleWithPermissionPage.setRecords(roleWithPermissions.subList(start, end));
        }
        roleWithPermissionPage.setTotal(roleWithPermissions.size());

        return roleWithPermissionPage;
    }

    @Override
    @Transactional
    public boolean saveRolePermissions(RoleWithPermission roleWithPermission) {
        // 校验参数
        if (roleWithPermission == null) {
            throw new BusinessException(400, "角色权限信息不能为空");
        }
        if (roleWithPermission.getId() == null || roleWithPermission.getId() <= 0) {
            throw new BusinessException(400, "角色ID不能为空且必须大于0");
        }

        // 记录日志
        logger.info("保存角色权限关联，角色ID：{}", roleWithPermission.getId());

        // 检查角色是否存在
        SysAuthRole role = sysAuthRoleService.getRoleById(roleWithPermission.getId());
        if (role == null) {
            logger.warn("角色不存在或已删除，角色ID：{}", roleWithPermission.getId());
            throw new BusinessException(404, "角色不存在或已删除");
        }

        // 检查权限是否存在
        if (roleWithPermission.getPermissions() != null && !roleWithPermission.getPermissions().isEmpty()) {
            for (SysAuthPermission permission : roleWithPermission.getPermissions()) {
                if (permission.getId() == null) {
                    logger.warn("权限ID不能为空");
                    throw new BusinessException(400, "权限ID不能为空");
                }
                SysAuthPermission existingPermission = sysAuthPermissionService.getPermissionById(permission.getId());
                if (existingPermission == null) {
                    logger.warn("权限不存在或已删除，权限ID：{}", permission.getId());
                    throw new BusinessException(404, "权限不存在或已删除，权限ID：" + permission.getId());
                }
            }
        }

        // 删除已有的角色权限关联
        this.remove(
            new QueryWrapper<SysAuthRolePermission>()
                .eq("role_id", roleWithPermission.getId())
        );

        // 保存新的角色权限关联
        if (roleWithPermission.getPermissions() != null && !roleWithPermission.getPermissions().isEmpty()) {
            List<SysAuthRolePermission> rolePermissions = new ArrayList<>();
            for (SysAuthPermission permission : roleWithPermission.getPermissions()) {
                SysAuthRolePermission rolePermission = new SysAuthRolePermission();
                rolePermission.setRoleId(roleWithPermission.getId());
                rolePermission.setPermissionId(permission.getId());
                rolePermission.setIsDeleted(0);
                rolePermissions.add(rolePermission);
            }
            boolean success = this.saveBatch(rolePermissions);
            if (!success) {
                logger.error("保存角色权限关联失败，角色ID：{}", roleWithPermission.getId());
                throw new BusinessException(500, "保存角色权限关联失败");
            }
        }

        logger.info("保存角色权限关联成功，角色ID：{}", roleWithPermission.getId());
        return true;
    }

    @Override
    @Transactional
    public boolean updateRolePermissions(RoleWithPermission roleWithPermission) {
        // 校验参数
        if (roleWithPermission == null) {
            throw new BusinessException(400, "角色权限信息不能为空");
        }
        if (roleWithPermission.getId() == null || roleWithPermission.getId() <= 0) {
            throw new BusinessException(400, "角色ID不能为空且必须大于0");
        }

        // 记录日志
        logger.info("更新角色权限关联，角色ID：{}", roleWithPermission.getId());

        // 检查角色是否存在
        SysAuthRole role = sysAuthRoleService.getRoleById(roleWithPermission.getId());
        if (role == null) {
            logger.warn("角色不存在或已删除，角色ID：{}", roleWithPermission.getId());
            throw new BusinessException(404, "角色不存在或已删除");
        }

        // 检查权限是否存在
        if (roleWithPermission.getPermissions() != null && !roleWithPermission.getPermissions().isEmpty()) {
            for (SysAuthPermission permission : roleWithPermission.getPermissions()) {
                if (permission.getId() == null) {
                    logger.warn("权限ID不能为空");
                    throw new BusinessException(400, "权限ID不能为空");
                }
                SysAuthPermission existingPermission = sysAuthPermissionService.getPermissionById(permission.getId());
                if (existingPermission == null) {
                    logger.warn("权限不存在或已删除，权限ID：{}", permission.getId());
                    throw new BusinessException(404, "权限不存在或已删除，权限ID：" + permission.getId());
                }
            }
        }

        // 删除已有的角色权限关联
        this.remove(
            new QueryWrapper<SysAuthRolePermission>()
                .eq("role_id", roleWithPermission.getId())
        );

        // 保存新的角色权限关联
        if (roleWithPermission.getPermissions() != null && !roleWithPermission.getPermissions().isEmpty()) {
            List<SysAuthRolePermission> rolePermissions = new ArrayList<>();
            for (SysAuthPermission permission : roleWithPermission.getPermissions()) {
                SysAuthRolePermission rolePermission = new SysAuthRolePermission();
                rolePermission.setRoleId(roleWithPermission.getId());
                rolePermission.setPermissionId(permission.getId());
                rolePermission.setIsDeleted(0);
                rolePermissions.add(rolePermission);
            }
            boolean success = this.saveBatch(rolePermissions);
            if (!success) {
                logger.error("更新角色权限关联失败，角色ID：{}", roleWithPermission.getId());
                throw new BusinessException(500, "更新角色权限关联失败");
            }
        }

        logger.info("更新角色权限关联成功，角色ID：{}", roleWithPermission.getId());
        return true;
    }

    @Override
    @Transactional
    public boolean deleteRolePermissions(Long roleId) {
        // 校验参数
        if (roleId == null || roleId <= 0) {
            throw new BusinessException(400, "角色ID不能为空且必须大于0");
        }

        // 记录日志
        logger.info("删除角色权限关联，角色ID：{}", roleId);

        // 检查角色是否存在
        SysAuthRole role = sysAuthRoleService.getRoleById(roleId);
        if (role == null) {
            logger.warn("角色不存在或已删除，角色ID：{}", roleId);
            throw new BusinessException(404, "角色不存在或已删除");
        }

        // 删除角色权限关联
        boolean success = this.remove(
            new QueryWrapper<SysAuthRolePermission>()
                .eq("role_id", roleId)
        );

        if (success) {
            logger.info("删除角色权限关联成功，角色ID：{}", roleId);
        } else {
            logger.error("删除角色权限关联失败，角色ID：{}", roleId);
            throw new BusinessException(500, "删除角色权限关联失败");
        }

        return success;
    }

    @Override
    @Transactional
    public boolean deleteBatchRolePermissions(List<Long> roleIds) {
        // 校验参数
        if (roleIds == null || roleIds.isEmpty()) {
            throw new BusinessException(400, "角色ID列表不能为空");
        }

        // 记录日志
        logger.info("批量删除角色权限关联，角色ID列表：{}", roleIds);

        // 检查角色是否存在
        for (Long roleId : roleIds) {
            if (roleId == null || roleId <= 0) {
                throw new BusinessException(400, "角色ID不能为空且必须大于0");
            }
            SysAuthRole role = sysAuthRoleService.getRoleById(roleId);
            if (role == null) {
                logger.warn("角色不存在或已删除，角色ID：{}", roleId);
                throw new BusinessException(404, "角色不存在或已删除，角色ID：" + roleId);
            }
        }

        // 批量删除角色权限关联
        boolean success = this.remove(
            new QueryWrapper<SysAuthRolePermission>()
                .in("role_id", roleIds)
        );

        if (success) {
            logger.info("批量删除角色权限关联成功，角色ID列表：{}", roleIds);
        } else {
            logger.error("批量删除角色权限关联失败，角色ID列表：{}", roleIds);
            throw new BusinessException(500, "批量删除角色权限关联失败");
        }

        return success;
    }
}





