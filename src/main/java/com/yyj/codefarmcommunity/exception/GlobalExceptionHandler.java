package com.yyj.codefarmcommunity.exception;

import com.yyj.codefarmcommunity.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
//@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理所有异常
     * @param e 异常对象
     * @return 统一响应
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        logger.error("全局异常捕获:", e);
        return Result.error("系统内部错误: " + e.getMessage());
    }
    
    /**
     * 处理业务异常
     * @param e 业务异常
     * @return 统一响应
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        logger.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理参数异常
     * @param e 参数异常
     * @return 统一响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("参数异常: {}", e.getMessage());
        return Result.badRequest("参数错误: " + e.getMessage());
    }
    
    /**
     * 处理空指针异常
     * @param e 空指针异常
     * @return 统一响应
     */
    @ExceptionHandler(NullPointerException.class)
    public Result handleNullPointerException(NullPointerException e) {
        logger.error("空指针异常:", e);
        return Result.error("系统错误: 空指针异常");
    }
}
