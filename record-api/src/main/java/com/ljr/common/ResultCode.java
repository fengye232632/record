package com.ljr.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    
    // 登录相关
    LOGIN_ERROR(1001, "用户名或密码错误"),
    ACCOUNT_LOCKED(1002, "账号已被锁定"),
    ACCOUNT_DISABLED(1003, "账号已被禁用"),
    UNAUTHORIZED(401, "未登录或token已过期"),
    
    // 参数相关
    PARAM_ERROR(2001, "参数错误"),
    
    // 系统相关
    SYSTEM_ERROR(9001, "系统错误");
    
    private final Integer code;
    private final String msg;

    /**
     * 获取状态码
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取状态信息
     */
    public String getMsg() {
        return msg;
    }
} 