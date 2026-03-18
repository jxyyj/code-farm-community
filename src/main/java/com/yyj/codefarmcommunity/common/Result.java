package com.yyj.codefarmcommunity.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一响应封装类
 */
public class Result {
    
    /**
     * 响应状态码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private Object data;
    
    /**
     * 其他额外信息
     */
    private Map<String, Object> extra;
    
    private Result() {
    }
    
    private Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.extra = new HashMap<>();
    }
    
    /**
     * 成功响应
     * @param data 响应数据
     * @return 响应对象
     */
    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }
    
    /**
     * 成功响应（无数据）
     * @return 响应对象
     */
    public static Result success() {
        return new Result(200, "操作成功", null);
    }
    
    /**
     * 成功响应（自定义消息）
     * @param message 响应消息
     * @param data 响应数据
     * @return 响应对象
     */
    public static Result success(String message, Object data) {
        return new Result(200, message, data);
    }
    
    /**
     * 失败响应
     * @param code 状态码
     * @param message 错误消息
     * @return 响应对象
     */
    public static Result error(int code, String message) {
        return new Result(code, message, null);
    }
    
    /**
     * 失败响应（默认500状态码）
     * @param message 错误消息
     * @return 响应对象
     */
    public static Result error(String message) {
        return new Result(500, message, null);
    }
    
    /**
     * 失败响应（默认400状态码）
     * @param message 错误消息
     * @return 响应对象
     */
    public static Result badRequest(String message) {
        return new Result(400, message, null);
    }
    
    /**
     * 失败响应（默认401状态码）
     * @param message 错误消息
     * @return 响应对象
     */
    public static Result unauthorized(String message) {
        return new Result(401, message, null);
    }
    
    /**
     * 失败响应（默认403状态码）
     * @param message 错误消息
     * @return 响应对象
     */
    public static Result forbidden(String message) {
        return new Result(403, message, null);
    }
    
    /**
     * 失败响应（默认404状态码）
     * @param message 错误消息
     * @return 响应对象
     */
    public static Result notFound(String message) {
        return new Result(404, message, null);
    }
    
    /**
     * 根据 ResultCode 创建响应
     * @param resultCode 状态码枚举
     * @param data 响应数据
     * @return 响应对象
     */
    public static Result of(ResultCode resultCode, Object data) {
        return new Result(resultCode.getCode(), resultCode.getMessage(), data);
    }
    
    /**
     * 根据 ResultCode 创建响应（无数据）
     * @param resultCode 状态码枚举
     * @return 响应对象
     */
    public static Result of(ResultCode resultCode) {
        return new Result(resultCode.getCode(), resultCode.getMessage(), null);
    }
    
    /**
     * 根据 ResultCode 创建成功响应
     * @param resultCode 状态码枚举
     * @param data 响应数据
     * @return 响应对象
     */
    public static Result success(ResultCode resultCode, Object data) {
        return new Result(resultCode.getCode(), resultCode.getMessage(), data);
    }
    
    /**
     * 根据 ResultCode 创建成功响应（无数据）
     * @param resultCode 状态码枚举
     * @return 响应对象
     */
    public static Result success(ResultCode resultCode) {
        return new Result(resultCode.getCode(), resultCode.getMessage(), null);
    }
    
    /**
     * 根据 ResultCode 创建失败响应
     * @param resultCode 状态码枚举
     * @return 响应对象
     */
    public static Result failure(ResultCode resultCode) {
        return new Result(resultCode.getCode(), resultCode.getMessage(), null);
    }
    
    /**
     * 根据 ResultCode 创建失败响应（自定义消息）
     * @param resultCode 状态码枚举
     * @param message 自定义消息
     * @return 响应对象
     */
    public static Result failure(ResultCode resultCode, String message) {
        return new Result(resultCode.getCode(), message, null);
    }
    
    /**
     * 添加额外信息
     * @param key 键
     * @param value 值
     * @return 响应对象
     */
    public Result addExtra(String key, Object value) {
        this.extra.put(key, value);
        return this;
    }
    
    // Getter and Setter
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public Map<String, Object> getExtra() {
        return extra;
    }
    
    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }
}
