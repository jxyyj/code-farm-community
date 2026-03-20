package com.yyj.codefarmcommunity.mapper;

import com.yyj.codefarmcommunity.entity.SysAuthUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 闫寅杰
 * @description 针对表【sys_auth_user(用户信息表)】的数据库操作Mapper
 * @createDate 2026-03-18 09:07:51
 * @Entity com.yyj.codefarmcommunity.entity.SysAuthUser
 */
public interface SysAuthUserMapper {
    int insert(SysAuthUser user);

    int updateById(SysAuthUser user);

    SysAuthUser selectById(@Param("id") Long id);

    SysAuthUser selectByUserName(@Param("userName") String userName);

    List<SysAuthUser> selectAll();

    List<SysAuthUser> selectByCondition(@Param("userName") String userName, @Param("status") Integer status);

    List<SysAuthUser> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    List<SysAuthUser> searchPage(@Param("offset") int offset, @Param("limit") int limit, @Param("userName") String userName, @Param("status") Integer status);

    long count();

    long countByCondition(@Param("userName") String userName, @Param("status") Integer status);
}




