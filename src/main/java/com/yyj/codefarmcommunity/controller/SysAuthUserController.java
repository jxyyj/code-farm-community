package com.yyj.codefarmcommunity.controller;

import com.yyj.codefarmcommunity.common.Result;
import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import com.yyj.codefarmcommunity.service.SysAuthPermissionService;
import com.yyj.codefarmcommunity.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户信息的增删改查操作")
public class SysAuthUserController {
    
    private final SysAuthUserService sysAuthUserService;
    private final SysAuthRoleService sysAuthRoleService;
    private final SysAuthPermissionService sysAuthPermissionService;
    
    public SysAuthUserController(SysAuthUserService sysAuthUserService, 
                               SysAuthRoleService sysAuthRoleService, 
                               SysAuthPermissionService sysAuthPermissionService) {
        this.sysAuthUserService = sysAuthUserService;
        this.sysAuthRoleService = sysAuthRoleService;
        this.sysAuthPermissionService = sysAuthPermissionService;
    }
    
    /**
     * 分页查询用户列表
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询用户列表", description = "根据页码和每页大小查询用户列表")
    public Result list(@RequestParam(defaultValue = "1") Integer page, 
                      @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(sysAuthUserService.listUsers(page, size));
    }
    
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户详细信息")
    public Result getById(@PathVariable Long id) {
        SysAuthUser user = sysAuthUserService.getUserById(id);
        return user != null ? Result.success(user) : Result.notFound("用户不存在");
    }
    
    /**
     * 新增用户
     * @param user 用户信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @Operation(summary = "新增用户", description = "创建新用户")
    public Result add(@RequestBody SysAuthUser user) {
        boolean success = sysAuthUserService.save(user);
        return success ? Result.success("新增成功") : Result.error("新增失败");
    }
    
    /**
     * 更新用户
     * @param user 用户信息
     * @return 操作结果
     */
    @PutMapping("/update")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public Result update(@RequestBody SysAuthUser user) {
        boolean success = sysAuthUserService.updateUser(user);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    public Result delete(@PathVariable Long id) {
        boolean success = sysAuthUserService.deleteUser(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    /**
     * 批量删除用户
     * @param ids 用户ID列表
     * @return 操作结果
     */
    @DeleteMapping("/delete/batch")
    @Operation(summary = "批量删除用户", description = "批量删除多个用户")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        boolean success = sysAuthUserService.deleteBatchUsers(ids);
        return success ? Result.success("批量删除成功") : Result.error("批量删除失败");
    }
    
    /**
     * 条件查询用户列表
     * @param page 页码
     * @param size 每页大小
     * @param userName 用户名
     * @param status 状态
     * @return 分页结果
     */
    @GetMapping("/search")
    @Operation(summary = "条件查询用户列表", description = "根据条件查询用户列表")
    public Result search(@RequestParam(defaultValue = "1") Integer page, 
                        @RequestParam(defaultValue = "10") Integer size, 
                        @RequestParam(required = false) String userName, 
                        @RequestParam(required = false) Integer status) {
        return Result.success(sysAuthUserService.searchUsers(page, size, userName, status));
    }
    
    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    @PostMapping("/{userId}/roles")
    @Operation(summary = "为用户分配角色", description = "为用户分配多个角色")
    public Result assignRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        boolean success = sysAuthUserService.assignRoles(userId, roleIds);
        return success ? Result.success("分配角色成功") : Result.error("分配角色失败");
    }
    
    /**
     * 回收用户角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    @DeleteMapping("/{userId}/roles")
    @Operation(summary = "回收用户角色", description = "回收用户的多个角色")
    public Result revokeRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        boolean success = sysAuthUserService.revokeRoles(userId, roleIds);
        return success ? Result.success("回收角色成功") : Result.error("回收角色失败");
    }
    
    /**
     * 获取当前用户信息
     * @param request 请求对象
     * @return 用户信息
     */
    @GetMapping("/current")
    @Operation(summary = "获取当前用户信息", description = "根据token获取当前用户信息")
    public Result getCurrentUser(HttpServletRequest request) {
        try {
            // 从请求头中获取token
            String token = null;
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
            
            if (token == null || token.isEmpty()) {
                return Result.error(401, "未认证: 请先登录");
            }
            
            // 验证token
            if (!JwtUtil.validateToken(token)) {
                return Result.error(401, "未认证: token已过期，请重新登录");
            }
            
            // 从token中获取用户信息
            Claims claims = JwtUtil.parseToken(token);
            String userId = claims.get("userId").toString();
            String userName = claims.get("userName").toString();
            // 安全地获取角色和权限列表
            List<String> roles = new ArrayList<>();
            List<String> permissions = new ArrayList<>();
            
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof List) {
                for (Object role : (List<?>) rolesObj) {
                    if (role instanceof String) {
                        roles.add((String) role);
                    }
                }
            }
            
            Object permissionsObj = claims.get("permissions");
            if (permissionsObj instanceof List) {
                for (Object permission : (List<?>) permissionsObj) {
                    if (permission instanceof String) {
                        permissions.add((String) permission);
                    }
                }
            }

            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("userId", Long.parseLong(userId));
            data.put("userName", userName);
            data.put("roles", roles);
            data.put("permissions", permissions);

            return Result.success(data);
        } catch (Exception e) {
            return Result.error(500, "获取用户信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户的角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    @GetMapping("/{userId}/roles")
    @Operation(summary = "获取用户的角色列表", description = "根据用户ID获取用户的角色列表")
    public Result getUserRoles(@PathVariable Long userId) {
        return Result.success(sysAuthRoleService.getRolesByUserId(userId));
    }
    
    /**
     * 获取用户的权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    @GetMapping("/{userId}/permissions")
    @Operation(summary = "获取用户的权限列表", description = "根据用户ID获取用户的权限列表")
    public Result getUserPermissions(@PathVariable Long userId) {
        return Result.success(sysAuthPermissionService.getPermissionsByUserId(userId));
    }

    /**
     * 统计用户总数
     * @return 用户总数
     */
    @GetMapping("/count")
    @Operation(summary = "统计用户总数", description = "统计所有未删除的用户数量")
    public Result count() {
        long count = sysAuthUserService.countUsers();
        return Result.success(count);
    }

    /**
     * 修改用户密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 操作结果
     */
    @PutMapping("/{userId}/password")
    @Operation(summary = "修改用户密码", description = "修改用户密码")
    public Result changePassword(@PathVariable Long userId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        boolean success = sysAuthUserService.changePassword(userId, oldPassword, newPassword);
        return success ? Result.success("密码修改成功") : Result.error("密码修改失败");
    }
}