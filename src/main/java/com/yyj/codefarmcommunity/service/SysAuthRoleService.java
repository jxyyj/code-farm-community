package com.yyj.codefarmcommunity.service;

import com.yyj.codefarmcommunity.entity.SysAuthRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_role(角色表)】的数据库操作Service
* @createDate 2026-03-18 09:07:51
*/
public interface SysAuthRoleService extends IService<SysAuthRole> {

    /**
     * 根据用户ID获取角色
     * @param userId 用户ID
     * @return 角色列表
     */
    List<String> getRolesByUserId(Long userId);

}

