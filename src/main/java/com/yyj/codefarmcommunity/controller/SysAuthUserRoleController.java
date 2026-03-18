package com.yyj.codefarmcommunity.controller;

import com.yyj.codefarmcommunity.common.Result;
import com.yyj.codefarmcommunity.entity.SysAuthUserRole;
import com.yyj.codefarmcommunity.service.SysAuthUserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 用户角色关联管理控制器
 */
@RestController
@RequestMapping("/api/auth/user-role")
@Tag(name = "用户角色关联管理", description = "用户角色关联信息的增删改查操作")
public class SysAuthUserRoleController {
    
    private final SysAuthUserRoleService sysAuthUserRoleService;
    
    public SysAuthUserRoleController(SysAuthUserRoleService sysAuthUserRoleService) {
        this.sysAuthUserRoleService = sysAuthUserRoleService;
    }
    
    /**
     * 分页查询用户角色关联列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询用户角色关联列表", description = "根据页码和每页大小查询用户角色关联列表")
    public Result list(@RequestParam(defaultValue = "1") Integer page, 
                      @RequestParam(defaultValue = "10") Integer size) {
        IPage<SysAuthUserRole> userRolePage = new Page<>(page, size);
        IPage<SysAuthUserRole> result = sysAuthUserRoleService.page(userRolePage);
        return Result.ok(result);
    }
    
    /**
     * 根据ID查询用户角色关联
     * @param id 关联ID
     * @return 关联信息
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID查询用户角色关联", description = "根据关联ID查询用户角色关联详细信息")
    public Result getById(@PathVariable Long id) {
        SysAuthUserRole userRole = sysAuthUserRoleService.getById(id);
        return userRole != null ? Result.ok(userRole) : Result.notFound("关联不存在");
    }
    
    /**
     * 新增用户角色关联
     * @param userRole 关联信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @Operation(summary = "新增用户角色关联", description = "创建新的用户角色关联")
    public Result add(@RequestBody SysAuthUserRole userRole) {
        boolean success = sysAuthUserRoleService.save(userRole);
        return success ? Result.ok("新增成功") : Result.error("新增失败");
    }
    
    /**
     * 更新用户角色关联
     * @param userRole 关联信息
     * @return 操作结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新用户角色关联", description = "更新用户角色关联信息")
    public Result update(@RequestBody SysAuthUserRole userRole) {
        boolean success = sysAuthUserRoleService.updateById(userRole);
        return success ? Result.ok("更新成功") : Result.error("更新失败");
    }
    
    /**
     * 删除用户角色关联
     * @param id 关联ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户角色关联", description = "根据关联ID删除用户角色关联")
    public Result delete(@PathVariable Long id) {
        boolean success = sysAuthUserRoleService.removeById(id);
        return success ? Result.ok("删除成功") : Result.error("删除失败");
    }
    
    /**
     * 条件查询用户角色关联列表
     * @param page 页码
     * @param size 每页大小
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 分页结果
     */
    @GetMapping("/search")
    @Operation(summary = "条件查询用户角色关联列表", description = "根据条件查询用户角色关联列表")
    public Result search(@RequestParam(defaultValue = "1") Integer page, 
                        @RequestParam(defaultValue = "10") Integer size, 
                        @RequestParam(required = false) Long userId, 
                        @RequestParam(required = false) Long roleId) {
        IPage<SysAuthUserRole> userRolePage = new Page<>(page, size);
        QueryWrapper<SysAuthUserRole> queryWrapper = new QueryWrapper<>();
        
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (roleId != null) {
            queryWrapper.eq("role_id", roleId);
        }
        
        IPage<SysAuthUserRole> result = sysAuthUserRoleService.page(userRolePage, queryWrapper);
        return Result.ok(result);
    }
}