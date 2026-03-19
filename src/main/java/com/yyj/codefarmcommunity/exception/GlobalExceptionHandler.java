package com.yyj.codefarmcommunity.exception;

import com.yyj.codefarmcommunity.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
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
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append("系统内部错误: " + e.getMessage());
        errorMsg.append("\n异常位置: " + getExceptionLocation(e));
        return Result.error(errorMsg.toString());
    }
    
    /**
     * 处理业务异常
     * @param e 业务异常
     * @return 统一响应
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        logger.warn("业务异常: {}", e.getMessage());
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append(e.getMessage());
        errorMsg.append("\n异常位置: " + getExceptionLocation(e));
        return Result.error(e.getCode(), errorMsg.toString());
    }
    
    /**
     * 处理参数异常
     * @param e 参数异常
     * @return 统一响应
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("参数异常: {}", e.getMessage());
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append("参数错误: " + e.getMessage());
        errorMsg.append("\n异常位置: " + getExceptionLocation(e));
        return Result.badRequest(errorMsg.toString());
    }
    
    /**
     * 处理空指针异常
     * @param e 空指针异常
     * @return 统一响应
     */
    @ExceptionHandler(NullPointerException.class)
    public Result handleNullPointerException(NullPointerException e) {
        logger.error("空指针异常:", e);
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append("系统错误: 空指针异常");
        errorMsg.append("\n异常位置: " + getExceptionLocation(e));
        return Result.error(errorMsg.toString());
    }
    
    /**
     * 获取异常发生的位置
     * @param e 异常对象
     * @return 异常位置信息
     */
    private String getExceptionLocation(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace.length > 0) {
            StackTraceElement element = stackTrace[0];
            return element.getClassName() + "." + element.getMethodName() + "() at line " + element.getLineNumber();
        }
        return "未知位置";
    }
}
