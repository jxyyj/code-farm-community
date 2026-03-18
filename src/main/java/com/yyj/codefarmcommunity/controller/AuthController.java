package com.yyj.codefarmcommunity.controller;

import com.yyj.codefarmcommunity.common.Result;
import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import com.yyj.codefarmcommunity.utils.JwtUtil;
import com.yyj.codefarmcommunity.vo.LoginRequest;
import com.yyj.codefarmcommunity.vo.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "登录、注册等认证操作")
public class AuthController {
    
    private final SysAuthUserService sysAuthUserService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    
    public AuthController(SysAuthUserService sysAuthUserService, 
                         AuthenticationManager authenticationManager, 
                         PasswordEncoder passwordEncoder) {
        this.sysAuthUserService = sysAuthUserService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * 登录
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录并获取 token")
    public Result login(@RequestBody LoginRequest loginRequest) {
        try {
            // 进行身份认证
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserName(),
                    loginRequest.getPassword()
                )
            );
            
            // 认证成功，设置到安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 从数据库中获取用户信息
            SysAuthUser user = sysAuthUserService.getOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SysAuthUser>()
                    .eq("user_name", loginRequest.getUserName())
            );
            
            // 生成 token
            String token = JwtUtil.generateToken(user.getId(), user.getUserName());
            
            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userName", user.getUserName());
            data.put("userId", user.getId());
            
            return Result.ok(data).addExtra("message", "登录成功");
        } catch (Exception e) {
            return Result.error(401, "用户名或密码错误");
        }
    }
    
    /**
     * 注册
     * @param registerRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册并获取 token")
    public Result register(@RequestBody RegisterRequest registerRequest) {
        try {
            // 检查用户是否已存在
            SysAuthUser existingUser = sysAuthUserService.getOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SysAuthUser>()
                    .eq("user_name", registerRequest.getUserName())
            );
            
            if (existingUser != null) {
                return Result.error(400, "用户名已存在");
            }
            
            // 创建新用户
            SysAuthUser user = new SysAuthUser();
            user.setUserName(registerRequest.getUserName());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setNickName(registerRequest.getNickName());
            user.setEmail(registerRequest.getEmail());
            user.setPhone(registerRequest.getPhone());
            user.setStatus(0); // 0 启用
            user.setIsDeleted(0); // 0 未删除
            
            // 保存用户到数据库
            sysAuthUserService.save(user);
            
            // 生成 token
            String token = JwtUtil.generateToken(user.getId(), user.getUserName());
            
            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userName", user.getUserName());
            data.put("userId", user.getId());
            
            return Result.ok(data).addExtra("message", "注册成功");
        } catch (Exception e) {
            return Result.error(500, "注册失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "根据 token 获取当前用户信息")
    public Result getCurrentUser() {
        try {
            // 从安全上下文获取认证信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            // 从数据库中获取用户信息
            SysAuthUser user = sysAuthUserService.getOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SysAuthUser>()
                    .eq("user_name", username)
            );
            
            if (user == null) {
                return Result.error(404, "用户不存在");
            }
            
            // 构建用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getId());
            userInfo.put("userName", user.getUserName());
            userInfo.put("nickName", user.getNickName());
            userInfo.put("email", user.getEmail());
            userInfo.put("phone", user.getPhone());
            userInfo.put("status", user.getStatus());
            
            return Result.ok(userInfo);
        } catch (Exception e) {
            return Result.error(500, "获取用户信息失败: " + e.getMessage());
        }
    }
}