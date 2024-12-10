package com.ljr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljr.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser login(@Param("username") String username, @Param("password") String password);
} 