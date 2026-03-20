package com.yyj.codefarmcommunity.mapper;

import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 闫寅杰
 * @description 针对表【sys_auth_user_role(用户角色表)】的数据库操作Mapper
 * @createDate 2026-03-18 09:07:51
 * @Entity com.yyj.codefarmcommunity.entity.SysAuthUserRole
 */
public interface SysAuthUserRoleMapper {
    int insert(SysAuthUserRole userRole);

    int updateById(SysAuthUserRole userRole);

    SysAuthUserRole selectById(@Param("id") Long id);

    List<SysAuthUserRole> selectByUserId(@Param("userId") Long userId);

    List<SysAuthUserRole> selectByRoleId(@Param("roleId") Long roleId);

    SysAuthUserRole selectByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int deleteByUserId(@Param("userId") Long userId);

    int deleteByUserIdAndRoleIds(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
}




