package com.ljr.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class R<T> {
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 返回信息
     */
    private String msg;
    
    /**
     * 返回数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 成功返回结果
     */
    public static <T> R<T> ok() {
        return ok(null);
    }

    /**
     * 成功返回结果
     * @param data 返回数据
     */
    public static <T> R<T> ok(T data) {
        return new R<T>()
                .setCode(ResultCode.SUCCESS.getCode())
                .setMsg(ResultCode.SUCCESS.getMsg())
                .setData(data);
    }

    /**
     * 失败返回结果
     */
    public static <T> R<T> fail() {
        return fail(ResultCode.ERROR);
    }

    /**
     * 失败返回结果
     * @param msg 错误信息
     */
    public static <T> R<T> fail(String msg) {
        return new R<T>()
                .setCode(ResultCode.ERROR.getCode())
                .setMsg(msg);
    }

    /**
     * 失败返回结果
     * @param resultCode 错误码
     */
    public static <T> R<T> fail(ResultCode resultCode) {
        return new R<T>()
                .setCode(resultCode.getCode())
                .setMsg(resultCode.getMsg());
    }

    /**
     * 失败返回结果
     * @param code 错误码
     * @param msg 错误信息
     */
    public static <T> R<T> fail(Integer code, String msg) {
        return new R<T>()
                .setCode(code)
                .setMsg(msg);
    }
} 