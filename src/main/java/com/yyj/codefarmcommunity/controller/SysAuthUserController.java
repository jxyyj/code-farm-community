package com.yyj.codefarmcommunity.controller;

import com.yyj.codefarmcommunity.common.Result;
import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户信息的增删改查操作")
public class SysAuthUserController {
    
    private final SysAuthUserService sysAuthUserService;
    
    public SysAuthUserController(SysAuthUserService sysAuthUserService) {
        this.sysAuthUserService = sysAuthUserService;
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
        IPage<SysAuthUser> userPage = new Page<>(page, size);
        IPage<SysAuthUser> result = sysAuthUserService.page(userPage);
        return Result.success(result);
    }
    
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/get/{id}")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID查询用户详细信息")
    public Result getById(@PathVariable Long id) {
        SysAuthUser user = sysAuthUserService.getById(id);
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
        boolean success = sysAuthUserService.updateById(user);
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
        boolean success = sysAuthUserService.removeById(id);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
    
    /**
     * 条件查询用户列表
     * @param page 页码
     * @param size 每页大小
     * @param userName 用户名
     * @param nickName 昵称
     * @param email 邮箱
     * @param status 状态
     * @return 分页结果
     */
    @GetMapping("/search")
    @Operation(summary = "条件查询用户列表", description = "根据条件查询用户列表")
    public Result search(@RequestParam(defaultValue = "1") Integer page, 
                        @RequestParam(defaultValue = "10") Integer size, 
                        @RequestParam(required = false) String userName, 
                        @RequestParam(required = false) String nickName, 
                        @RequestParam(required = false) String email, 
                        @RequestParam(required = false) Integer status) {
        IPage<SysAuthUser> userPage = new Page<>(page, size);
        QueryWrapper<SysAuthUser> queryWrapper = new QueryWrapper<>();
        
        if (userName != null && !userName.isEmpty()) {
            queryWrapper.like("user_name", userName);
        }
        if (nickName != null && !nickName.isEmpty()) {
            queryWrapper.like("nick_name", nickName);
        }
        if (email != null && !email.isEmpty()) {
            queryWrapper.like("email", email);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        IPage<SysAuthUser> result = sysAuthUserService.page(userPage, queryWrapper);
        return Result.success(result);
    }
}