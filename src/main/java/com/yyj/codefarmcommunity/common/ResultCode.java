package com.yyj.codefarmcommunity.common;

/**
 * 响应状态码枚举类
 */
public enum ResultCode {
    
    // ================================== 成功相关 ==================================
    /** 成功 */
    SUCCESS(200, "操作成功"),
    
    // ================================== 客户端错误 ==================================
    /** 请求参数错误 */
    BAD_REQUEST(400, "请求参数错误"),
    /** 未授权 */
    UNAUTHORIZED(401, "未授权"),
    /** 禁止访问 */
    FORBIDDEN(403, "禁止访问"),
    /** 资源不存在 */
    NOT_FOUND(404, "资源不存在"),
    /** 请求方法错误 */
    METHOD_NOT_ALLOWED(405, "请求方法错误"),
    
    // ================================== 服务器错误 ==================================
    /** 服务器内部错误 */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    /** 服务不可用 */
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    
    // ================================== 业务错误 ==================================
    /** 业务逻辑错误 */
    BUSINESS_ERROR(1001, "业务逻辑错误"),
    /** 数据校验错误 */
    VALIDATION_ERROR(1002, "数据校验错误"),
    /** 数据库操作错误 */
    DATABASE_ERROR(1003, "数据库操作错误"),
    /** 文件上传错误 */
    FILE_UPLOAD_ERROR(1004, "文件上传错误"),
    /** 网络请求错误 */
    NETWORK_ERROR(1005, "网络请求错误"),
    
    // ================================== 用户相关 ==================================
    /** 用户不存在 */
    USER_NOT_FOUND(2001, "用户不存在"),
    /** 用户名或密码错误 */
    USER_PASSWORD_ERROR(2002, "用户名或密码错误"),
    /** 用户已存在 */
    USER_EXIST(2003, "用户已存在"),
    /** 用户被禁用 */
    USER_DISABLED(2004, "用户被禁用"),
    
    // ================================== 权限相关 ==================================
    /** 权限不足 */
    PERMISSION_DENIED(3001, "权限不足"),
    /** 角色不存在 */
    ROLE_NOT_FOUND(3002, "角色不存在"),
    /** 权限不存在 */
    PERMISSION_NOT_FOUND(3003, "权限不存在"),
    
    // ================================== 其他错误 ==================================
    /** 未知错误 */
    UNKNOWN_ERROR(9999, "未知错误");
    
    /** 状态码 */
    private final int code;
    /** 状态信息 */
    private final String message;
    
    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    /**
     * 根据状态码获取枚举
     * @param code 状态码
     * @return ResultCode枚举
     */
    public static ResultCode getByCode(int code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.code == code) {
                return resultCode;
            }
        }
        return UNKNOWN_ERROR;
    }
}
