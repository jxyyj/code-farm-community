package com.yyj.codefarmcommunity.controller;

import com.yyj.codefarmcommunity.common.Result;
import com.yyj.codefarmcommunity.exception.BusinessException;
import com.yyj.codefarmcommunity.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/api/test")
@Tag(name = "测试接口", description = "用于测试系统功能的接口")
public class TestController {
    
    private final TestService testService;
    
    public TestController(TestService testService) {
        this.testService = testService;
    }

    /**
     * 测试接口
     * @return 测试响应
     */
    @GetMapping("/hello")
    @Operation(summary = "测试hello接口", description = "返回简单的hello消息")
    public Result hello() {
        return Result.ok("Hello, Code Farm Community!");
    }

    /**
     * 健康检查接口
     * @return 健康状态
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查接口", description = "检查系统健康状态")
    public Result health() {
        return Result.ok().addExtra("status", "UP").addExtra("message", "系统运行正常");
    }

    /**
     * 获取系统信息接口
     * @return 系统信息
     */
    @GetMapping("/info")
    @Operation(summary = "系统信息接口", description = "获取系统基本信息")
    public Result info() {
        return Result.ok()
                .addExtra("name", "Code Farm Community")
                .addExtra("version", "1.0.0")
                .addExtra("description", "基于Spring Boot的社区项目")
                .addExtra("status", "running");
    }

    /**
     * 测试异常处理接口
     * @return 响应
     */
    @GetMapping("/exception")
    @Operation(summary = "测试异常处理", description = "测试全局异常捕获")
    public Result testException() {
        throw new BusinessException("测试业务异常");
    }

    /**
     * 测试空指针异常处理接口
     * @return 响应
     */
    @GetMapping("/null")
    @Operation(summary = "测试空指针异常", description = "测试空指针异常捕获")
    public Result testNullPointerException() {
        String str = null;
        str.length(); // 触发空指针异常
        return Result.ok();
    }

    /**
     * 测试参数异常处理接口
     * @return 响应
     */
    @GetMapping("/param")
    @Operation(summary = "测试参数异常", description = "测试参数异常捕获")
    public Result testIllegalArgumentException() {
        throw new IllegalArgumentException("参数错误：参数不能为空");
    }
    
    /**
     * 测试业务层调用
     * @return 响应
     */
    @GetMapping("/service")
    @Operation(summary = "测试业务层调用", description = "测试业务层日志切面")
    public Result testService() {
        String result = testService.test("Code Farm");
        return Result.ok(result);
    }
    
    /**
     * 测试业务层异常
     * @return 响应
     */
    @GetMapping("/service/exception")
    @Operation(summary = "测试业务层异常", description = "测试业务层异常处理")
    public Result testServiceException() {
        try {
            testService.testException("测试业务层异常");
            return Result.ok();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 测试业务层计算
     * @return 响应
     */
    @GetMapping("/service/calculate")
    @Operation(summary = "测试业务层计算", description = "测试业务层方法调用")
    public Result testServiceCalculate() {
        int result = testService.calculate(10, 20);
        return Result.ok("计算结果：" + result);
    }
}
