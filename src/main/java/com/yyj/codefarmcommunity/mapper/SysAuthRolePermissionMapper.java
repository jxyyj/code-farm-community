package com.yyj.codefarmcommunity.mapper;

import com.yyj.codefarmcommunity.entity.SysAuthRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 闫寅杰
 * @description 针对表【sys_auth_role_permission(角色权限表)】的数据库操作Mapper
 * @createDate 2026-03-18 09:07:51
 * @Entity com.yyj.codefarmcommunity.entity.SysAuthRolePermission
 */
public interface SysAuthRolePermissionMapper {
    int insert(SysAuthRolePermission rolePermission);

    int updateById(SysAuthRolePermission rolePermission);

    SysAuthRolePermission selectById(@Param("id") Long id);

    List<SysAuthRolePermission> selectByRoleId(@Param("roleId") Long roleId);

    List<SysAuthRolePermission> selectByPermissionId(@Param("permissionId") Long permissionId);

    SysAuthRolePermission selectByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    int deleteByRoleId(@Param("roleId") Long roleId);

    int deleteByRoleIdAndPermissionIds(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);

    int deleteByPermissionId(@Param("permissionId") Long permissionId);
}




