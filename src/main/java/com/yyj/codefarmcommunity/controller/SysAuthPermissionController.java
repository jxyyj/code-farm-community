package com.yyj.codefarmcommunity.controller;

import com.yyj.codefarmcommunity.common.Result;
import com.yyj.codefarmcommunity.entity.SysAuthPermission;
import com.yyj.codefarmcommunity.service.SysAuthPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/auth/permission")
@Tag(name = "权限管理", description = "权限信息的增删改查操作")
public class SysAuthPermissionController {
    
    private final SysAuthPermissionService sysAuthPermissionService;
    
    public SysAuthPermissionController(SysAuthPermissionService sysAuthPermissionService) {
        this.sysAuthPermissionService = sysAuthPermissionService;
    }
    
    /**
     * 分页查询权限列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询权限列表", description = "根据页码和每页大小查询权限列表")
    public Result list(@RequestParam(defaultValue = "1") Integer page, 
                      @RequestParam(defaultValue = "10") Integer size) {
        IPage<SysAuthPermission> permissionPage = new Page<>(page, size);
        IPage<SysAuthPermission> result = sysAuthPermissionService.page(permissionPage);
        return Result.ok(result);
    }
    
    /**
     * 根据ID查询权限
     * @param id 权限ID
     * @return 权限信息
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID查询权限", description = "根据权限ID查询权限详细信息")
    public Result getById(@PathVariable Long id) {
        SysAuthPermission permission = sysAuthPermissionService.getById(id);
        return permission != null ? Result.ok(permission) : Result.notFound("权限不存在");
    }
    
    /**
     * 新增权限
     * @param permission 权限信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @Operation(summary = "新增权限", description = "创建新权限")
    public Result add(@RequestBody SysAuthPermission permission) {
        boolean success = sysAuthPermissionService.save(permission);
        return success ? Result.ok("新增成功") : Result.error("新增失败");
    }
    
    /**
     * 更新权限
     * @param permission 权限信息
     * @return 操作结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新权限", description = "更新权限信息")
    public Result update(@RequestBody SysAuthPermission permission) {
        boolean success = sysAuthPermissionService.updateById(permission);
        return success ? Result.ok("更新成功") : Result.error("更新失败");
    }
    
    /**
     * 删除权限
     * @param id 权限ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除权限", description = "根据权限ID删除权限")
    public Result delete(@PathVariable Long id) {
        boolean success = sysAuthPermissionService.removeById(id);
        return success ? Result.ok("删除成功") : Result.error("删除失败");
    }
    
    /**
     * 条件查询权限列表
     * @param page 页码
     * @param size 每页大小
     * @param name 权限名称
     * @param type 权限类型
     * @param status 状态
     * @param parentId 父菜单ID
     * @return 分页结果
     */
    @GetMapping("/search")
    @Operation(summary = "条件查询权限列表", description = "根据条件查询权限列表")
    public Result search(@RequestParam(defaultValue = "1") Integer page, 
                        @RequestParam(defaultValue = "10") Integer size, 
                        @RequestParam(required = false) String name, 
                        @RequestParam(required = false) Integer type, 
                        @RequestParam(required = false) Integer status, 
                        @RequestParam(required = false) Long parentId) {
        IPage<SysAuthPermission> permissionPage = new Page<>(page, size);
        QueryWrapper<SysAuthPermission> queryWrapper = new QueryWrapper<>();
        
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (parentId != null) {
            queryWrapper.eq("parent_id", parentId);
        }
        
        IPage<SysAuthPermission> result = sysAuthPermissionService.page(permissionPage, queryWrapper);
        return Result.ok(result);
    }
}