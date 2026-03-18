package com.yyj.codefarmcommunity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 日志切面类
 */
@Aspect
@Component
public class LogAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    
    /**
     * 定义切点，拦截所有controller包下的方法
     */
    @Pointcut("execution(* com.yyj.codefarmcommunity.controller..*.*(..))")
    public void controllerPointcut() {
    }
    
    /**
     * 环绕通知，记录请求和响应信息
     * @param joinPoint 连接点
     * @return 方法返回值
     * @throws Throwable 异常
     */
    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开始时间
        long startTime = System.currentTimeMillis();
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        
        // 记录请求信息
        logger.info("[API请求] 请求路径: {}", request.getRequestURI());
        logger.info("[API请求] 请求方法: {}", request.getMethod());
        logger.info("[API请求] 客户端IP: {}", request.getRemoteAddr());
        logger.info("[API请求] 处理方法: {}.{}", className, methodName);
        logger.info("[API请求] 请求参数: {}", Arrays.toString(joinPoint.getArgs()));
        
        try {
            // 执行方法
            Object result = joinPoint.proceed();
            
            // 计算执行时间
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            
            // 记录响应信息
            logger.info("[API响应] 处理方法: {}.{}", className, methodName);
            logger.info("[API响应] 响应结果: {}", result);
            logger.info("[API响应] 执行时间: {}ms", costTime);
            
            return result;
        } catch (Exception e) {
            // 记录异常信息
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            
            logger.error("[API异常] 处理方法: {}.{}", className, methodName);
            logger.error("[API异常] 异常信息: {}", e.getMessage());
            logger.error("[API异常] 执行时间: {}ms", costTime);
            
            throw e;
        }
    }
}
