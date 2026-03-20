package com.yyj.codefarmcommunity.mapper;

import com.yyj.codefarmcommunity.entity.SysAuthRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 闫寅杰
 * @description 针对表【sys_auth_role(角色表)】的数据库操作Mapper
 * @createDate 2026-03-18 09:07:51
 * @Entity com.yyj.codefarmcommunity.entity.SysAuthRole
 */
public interface SysAuthRoleMapper {
    int insert(SysAuthRole role);

    int updateById(SysAuthRole role);

    SysAuthRole selectById(@Param("id") Long id);

    SysAuthRole selectByRoleKey(@Param("roleKey") String roleKey);

    List<SysAuthRole> selectAll();

    List<SysAuthRole> selectByCondition(@Param("roleName") String roleName, @Param("status") Integer status);

    long count();
}




