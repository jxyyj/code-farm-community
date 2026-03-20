package com.yyj.codefarmcommunity.mapper;

import com.yyj.codefarmcommunity.entity.SysAuthPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 闫寅杰
 * @description 针对表【sys_auth_permission(权限表)】的数据库操作Mapper
 * @createDate 2026-03-18 09:07:51
 * @Entity com.yyj.codefarmcommunity.entity.SysAuthPermission
 */
public interface SysAuthPermissionMapper {
    int insert(SysAuthPermission permission);

    int updateById(SysAuthPermission permission);

    SysAuthPermission selectById(@Param("id") Long id);

    SysAuthPermission selectByPermissionKey(@Param("permissionKey") String permissionKey);

    List<SysAuthPermission> selectAll();

    List<SysAuthPermission> selectByCondition(@Param("name") String name, @Param("type") Integer type, @Param("status") Integer status, @Param("parentId") Long parentId);

    List<SysAuthPermission> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    List<SysAuthPermission> searchPage(@Param("offset") int offset, @Param("limit") int limit, @Param("name") String name, @Param("type") Integer type, @Param("status") Integer status, @Param("parentId") Long parentId);

    long count();

    long countByCondition(@Param("name") String name, @Param("type") Integer type, @Param("status") Integer status, @Param("parentId") Long parentId);
}




