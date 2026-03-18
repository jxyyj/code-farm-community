package com.yyj.codefarmcommunity.service;

import com.yyj.codefarmcommunity.entity.SysAuthPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 闫寅杰
* @description 针对表【sys_auth_permission(权限表)】的数据库操作Service
* @createDate 2026-03-18 09:07:51
*/
public interface SysAuthPermissionService extends IService<SysAuthPermission> {

    /**
     * 根据用户ID获取权限
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getPermissionsByUserId(Long userId);

}

