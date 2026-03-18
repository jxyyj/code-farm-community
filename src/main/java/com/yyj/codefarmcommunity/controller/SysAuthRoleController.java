package com.yyj.codefarmcommunity.controller;

import com.yyj.codefarmcommunity.common.Result;
import com.yyj.codefarmcommunity.entity.SysAuthRole;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/role")
@Tag(name = "角色管理", description = "角色信息的增删改查操作")
public class SysAuthRoleController {
    
    private final SysAuthRoleService sysAuthRoleService;
    
    public SysAuthRoleController(SysAuthRoleService sysAuthRoleService) {
        this.sysAuthRoleService = sysAuthRoleService;
    }
    
    /**
     * 分页查询角色列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询角色列表", description = "根据页码和每页大小查询角色列表")
    public Result list(@RequestParam(defaultValue = "1") Integer page, 
                      @RequestParam(defaultValue = "10") Integer size) {
        IPage<SysAuthRole> rolePage = new Page<>(page, size);
        IPage<SysAuthRole> result = sysAuthRoleService.page(rolePage);
        return Result.success(result);
    }
    
    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID查询角色", description = "根据角色ID查询角色详细信息")
    public Result getById(@PathVariable Long id) {
        SysAuthRole role = sysAuthRoleService.getById(id);
        return role != null ? Result.success(role) : Result.notFound("角色不存在");
    }
    
    /**
     * 新增角色
     * @param role 角色信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @Operation(summary = "新增角色", description = "创建新角色")
    public Result add(@RequestBody SysAuthRole role) {
        boolean success = sysAuthRoleService.save(role);
        return success ? Result.success("新增成功") : Result.error("新增失败");
    }
    
    /**
     * 更新角色
     * @param role 角色信息
     * @return 操作结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新角色", description = "更新角色信息")
    public Result update(@RequestBody SysAuthRole role) {
        boolean success = sysAuthRoleService.updateById(role);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    /**
     * 删除角色
     * @param id 角色ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除角色", description = "根据角色ID删除角色")
    public Result delete(@PathVariable Long id) {
        boolean success = sysAuthRoleService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    /**
     * 条件查询角色列表
     * @param page 页码
     * @param size 每页大小
     * @param roleName 角色名称
     * @param roleKey 角色唯一标识
     * @return 分页结果
     */
    @GetMapping("/search")
    @Operation(summary = "条件查询角色列表", description = "根据条件查询角色列表")
    public Result search(@RequestParam(defaultValue = "1") Integer page, 
                        @RequestParam(defaultValue = "10") Integer size, 
                        @RequestParam(required = false) String roleName, 
                        @RequestParam(required = false) String roleKey) {
        IPage<SysAuthRole> rolePage = new Page<>(page, size);
        QueryWrapper<SysAuthRole> queryWrapper = new QueryWrapper<>();
        
        if (roleName != null && !roleName.isEmpty()) {
            queryWrapper.like("role_name", roleName);
        }
        if (roleKey != null && !roleKey.isEmpty()) {
            queryWrapper.like("role_key", roleKey);
        }
        
        IPage<SysAuthRole> result = sysAuthRoleService.page(rolePage, queryWrapper);
        return Result.success(result);
    }
}