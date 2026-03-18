package com.yyj.codefarmcommunity.exception;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {
    
    /**
     * 错误码
     */
    private int code;
    
    /**
     * 构造方法
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }
    
    /**
     * 构造方法
     * @param code 错误码
     * @param message 错误消息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    /**
     * 构造方法
     * @param message 错误消息
     * @param cause 异常原因
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }
    
    /**
     * 构造方法
     * @param code 错误码
     * @param message 错误消息
     * @param cause 异常原因
     */
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    // Getter
    public int getCode() {
        return code;
    }
    
    // Setter
    public void setCode(int code) {
        this.code = code;
    }
}
