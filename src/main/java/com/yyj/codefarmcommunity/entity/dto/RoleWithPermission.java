package com.yyj.codefarmcommunity.entity.dto;

import com.yyj.codefarmcommunity.entity.SysAuthPermission;
import com.yyj.codefarmcommunity.entity.SysAuthRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleWithPermission extends SysAuthRole {
    private List<SysAuthPermission> permissions;
}
