package com.yyj.codefarmcommunity.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 业务层日志切面类
 */
@Aspect
@Component
public class ServiceLogAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
    
    /**
     * 定义切点，拦截所有service包下的方法
     */
    @Pointcut("execution(* com.yyj.codefarmcommunity.service..*.*(..))")
    public void servicePointcut() {
    }
    
    /**
     * 环绕通知，记录业务层方法的调用信息
     * @param joinPoint 连接点
     * @return 方法返回值
     * @throws Throwable 异常
     */
    @Around("servicePointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开始时间
        long startTime = System.currentTimeMillis();
        
        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        
        // 记录方法调用开始
        logger.info("[业务层] 方法调用开始: {}.{}", className, methodName);
        logger.info("[业务层] 方法参数: {}", Arrays.toString(joinPoint.getArgs()));
        
        try {
            // 执行方法
            Object result = joinPoint.proceed();
            
            // 计算执行时间
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            
            // 记录方法调用成功
            logger.info("[业务层] 方法调用成功: {}.{}", className, methodName);
            logger.info("[业务层] 方法返回值: {}", result);
            logger.info("[业务层] 执行时间: {}ms", costTime);
            
            return result;
        } catch (Exception e) {
            // 记录方法调用异常
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            
            logger.error("[业务层] 方法调用异常: {}.{}", className, methodName);
            logger.error("[业务层] 异常信息: {}", e.getMessage());
            logger.error("[业务层] 执行时间: {}ms", costTime);
            
            throw e;
        }
    }
}
