package com.yyj.codefarmcommunity.controller;

import com.yyj.codefarmcommunity.common.Result;
import com.yyj.codefarmcommunity.entity.SysAuthUser;
import com.yyj.codefarmcommunity.service.SysAuthRoleService;
import com.yyj.codefarmcommunity.service.SysAuthUserService;
import com.yyj.codefarmcommunity.service.SysAuthPermissionService;
import com.yyj.codefarmcommunity.utils.JwtUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yyj.codefarmcommunity.vo.LoginRequest;
import com.yyj.codefarmcommunity.vo.RegisterRequest;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api")
@Tag(name = "认证管理", description = "登录、注册等认证操作")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final SysAuthUserService sysAuthUserService;
    private final AuthenticationManager authenticationManager;
    private final SysAuthRoleService sysAuthRoleService;
    private final SysAuthPermissionService sysAuthPermissionService;
    
    public AuthController(SysAuthUserService sysAuthUserService, 
                         AuthenticationManager authenticationManager,
                         SysAuthRoleService sysAuthRoleService,
                         SysAuthPermissionService sysAuthPermissionService) {
        this.sysAuthUserService = sysAuthUserService;
        this.authenticationManager = authenticationManager;
        this.sysAuthRoleService = sysAuthRoleService;
        this.sysAuthPermissionService = sysAuthPermissionService;
    }
    
    /**
     * 登录
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录并获取 token")
    public Result login(@RequestBody LoginRequest loginRequest) {
        logger.info("用户登录请求: {}", loginRequest.getUserName());
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
            SysAuthUser user = getUserByUserName(loginRequest.getUserName());
            
            if (user == null) {
                logger.warn("用户不存在: {}", loginRequest.getUserName());
                return Result.error(404, "用户不存在");
            }
            
            // 获取用户角色和权限
            List<String> roles = sysAuthRoleService.getRolesByUserId(user.getId());
            List<String> permissions = sysAuthPermissionService.getPermissionsByUserId(user.getId());
            
            // 构建token的信息
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("userName", user.getUserName());
            claims.put("roles", roles);
            claims.put("permissions", permissions);
            
            // 生成 token
            String token = JwtUtil.generateToken(claims);
            
            logger.info("用户登录成功: {}", loginRequest.getUserName());
            return Result.success(claims).addExtra("token:", token);
        } catch (Exception e) {
            logger.error("用户登录失败: {}", loginRequest.getUserName(), e);
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
        logger.info("用户注册请求: {}", registerRequest.getUserName());
        try {
            // 调用服务层的注册方法（包含事务）
            SysAuthUser user = sysAuthUserService.register(registerRequest);

            // 获取用户角色和权限
            List<String> roles = sysAuthRoleService.getRolesByUserId(user.getId());
            List<String> permissions = sysAuthPermissionService.getPermissionsByUserId(user.getId());
            
            // 构建token的信息
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("userName", user.getUserName());
            claims.put("roles", roles);
            claims.put("permissions", permissions);
            
            // 生成 token
            String token = JwtUtil.generateToken(claims);
            
            logger.info("用户注册成功: {}", registerRequest.getUserName());
            return Result.success(claims).addExtra("token:", token);
        } catch (RuntimeException e) {
            logger.error("用户注册失败: {}", registerRequest.getUserName(), e);
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            logger.error("用户注册失败: {}", registerRequest.getUserName(), e);
            return Result.error(500, "注册失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息", description = "根据 token 获取当前用户信息")
    public Result getCurrentUser(HttpServletRequest request) {
        try {
            // 从请求头中获取 token
            String token = null;
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
            
            if (token == null || token.isEmpty()) {
                logger.warn("未提供 token");
                return Result.error(401, "未认证: 请先登录");
            }
            
            // 验证 token
            if (!JwtUtil.validateToken(token)) {
                logger.warn("token 无效或已过期");
                return Result.error(401, "未认证: token 已过期，请重新登录");
            }
            
            // 从 token 中获取用户信息
            Claims claims = JwtUtil.parseToken(token);
            // 从 claims 中获取用户信息
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
            logger.error("获取用户信息失败", e);
            return Result.error(500, "获取用户信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据用户名获取用户信息
     * @param userName 用户名
     * @return 用户信息
     */
    private SysAuthUser getUserByUserName(String userName) {
        return sysAuthUserService.getOne(
            new QueryWrapper<SysAuthUser>()
                .eq("user_name", userName)
        );
    }
}