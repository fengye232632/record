package com.ljr.service;

import com.ljr.entity.SysUser;

public interface ISysUserService  {
    /**
     * 用户登录
     */
    SysUser login(String username, String password);
} 