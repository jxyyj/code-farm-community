package com.yyj.codefarmcommunity.controller;

import com.yyj.codefarmcommunity.common.Result;
import com.yyj.codefarmcommunity.entity.SysAuthRolePermission;
import com.yyj.codefarmcommunity.service.SysAuthRolePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 角色权限关联管理控制器
 */
@RestController
@RequestMapping("/api/auth/role-permission")
@Tag(name = "角色权限关联管理", description = "角色权限关联信息的增删改查操作")
public class SysAuthRolePermissionController {
    
    private final SysAuthRolePermissionService sysAuthRolePermissionService;
    
    public SysAuthRolePermissionController(SysAuthRolePermissionService sysAuthRolePermissionService) {
        this.sysAuthRolePermissionService = sysAuthRolePermissionService;
    }
    
    /**
     * 分页查询角色权限关联列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询角色权限关联列表", description = "根据页码和每页大小查询角色权限关联列表")
    public Result list(@RequestParam(defaultValue = "1") Integer page, 
                      @RequestParam(defaultValue = "10") Integer size) {
        IPage<SysAuthRolePermission> rolePermissionPage = new Page<>(page, size);
        IPage<SysAuthRolePermission> result = sysAuthRolePermissionService.page(rolePermissionPage);
        return Result.ok(result);
    }
    
    /**
     * 根据ID查询角色权限关联
     * @param id 关联ID
     * @return 关联信息
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID查询角色权限关联", description = "根据关联ID查询角色权限关联详细信息")
    public Result getById(@PathVariable Long id) {
        SysAuthRolePermission rolePermission = sysAuthRolePermissionService.getById(id);
        return rolePermission != null ? Result.ok(rolePermission) : Result.notFound("关联不存在");
    }
    
    /**
     * 新增角色权限关联
     * @param rolePermission 关联信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @Operation(summary = "新增角色权限关联", description = "创建新的角色权限关联")
    public Result add(@RequestBody SysAuthRolePermission rolePermission) {
        boolean success = sysAuthRolePermissionService.save(rolePermission);
        return success ? Result.ok("新增成功") : Result.error("新增失败");
    }
    
    /**
     * 更新角色权限关联
     * @param rolePermission 关联信息
     * @return 操作结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新角色权限关联", description = "更新角色权限关联信息")
    public Result update(@RequestBody SysAuthRolePermission rolePermission) {
        boolean success = sysAuthRolePermissionService.updateById(rolePermission);
        return success ? Result.ok("更新成功") : Result.error("更新失败");
    }
    
    /**
     * 删除角色权限关联
     * @param id 关联ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除角色权限关联", description = "根据关联ID删除角色权限关联")
    public Result delete(@PathVariable Long id) {
        boolean success = sysAuthRolePermissionService.removeById(id);
        return success ? Result.ok("删除成功") : Result.error("删除失败");
    }
    
    /**
     * 条件查询角色权限关联列表
     * @param page 页码
     * @param size 每页大小
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 分页结果
     */
    @GetMapping("/search")
    @Operation(summary = "条件查询角色权限关联列表", description = "根据条件查询角色权限关联列表")
    public Result search(@RequestParam(defaultValue = "1") Integer page, 
                        @RequestParam(defaultValue = "10") Integer size, 
                        @RequestParam(required = false) Long roleId, 
                        @RequestParam(required = false) Long permissionId) {
        IPage<SysAuthRolePermission> rolePermissionPage = new Page<>(page, size);
        QueryWrapper<SysAuthRolePermission> queryWrapper = new QueryWrapper<>();
        
        if (roleId != null) {
            queryWrapper.eq("role_id", roleId);
        }
        if (permissionId != null) {
            queryWrapper.eq("permission_id", permissionId);
        }
        
        IPage<SysAuthRolePermission> result = sysAuthRolePermissionService.page(rolePermissionPage, queryWrapper);
        return Result.ok(result);
    }
}