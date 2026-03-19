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
import java.util.List;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/permission")
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
        return Result.success(sysAuthPermissionService.listPermissions(page, size));
    }
    
    /**
     * 根据ID查询权限
     * @param id 权限ID
     * @return 权限信息
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID查询权限", description = "根据权限ID查询权限详细信息")
    public Result getById(@PathVariable Long id) {
        SysAuthPermission permission = sysAuthPermissionService.getPermissionByIdWithCheck(id);
        return Result.success(permission);
    }
    
    /**
     * 新增权限
     * @param permission 权限信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @Operation(summary = "新增权限", description = "创建新权限")
    public Result add(@RequestBody SysAuthPermission permission) {
        boolean success = sysAuthPermissionService.addPermission(permission);
        return success ? Result.success("新增成功") : Result.error("新增失败");
    }
    
    /**
     * 更新权限
     * @param permission 权限信息
     * @return 操作结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新权限", description = "更新权限信息")
    public Result update(@RequestBody SysAuthPermission permission) {
        boolean success = sysAuthPermissionService.updatePermission(permission);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    /**
     * 删除权限
     * @param id 权限ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除权限", description = "根据权限ID删除权限")
    public Result delete(@PathVariable Long id) {
        boolean success = sysAuthPermissionService.deletePermission(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
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
        return Result.success(sysAuthPermissionService.searchPermissions(page, size, name, type, status, parentId));
    }
    
    /**
     * 查询所有权限
     * @return 权限列表
     */
    @GetMapping("/all")
    @Operation(summary = "查询所有权限", description = "查询所有未删除的权限列表")
    public Result getAllPermissions() {
        return Result.success(sysAuthPermissionService.getAllPermissions());
    }
    
    /**
     * 批量删除权限
     * @param ids 权限ID列表
     * @return 操作结果
     */
    @DeleteMapping("/delete/batch")
    @Operation(summary = "批量删除权限", description = "批量删除多个权限")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        boolean success = sysAuthPermissionService.deleteBatchPermissions(ids);
        return success ? Result.success("批量删除成功") : Result.error("批量删除失败");
    }

    /**
     * 统计权限总数
     * @return 权限总数
     */
    @GetMapping("/count")
    @Operation(summary = "统计权限总数", description = "统计所有未删除的权限数量")
    public Result count() {
        long count = sysAuthPermissionService.countPermissions();
        return Result.success(count);
    }

}