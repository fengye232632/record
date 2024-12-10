package com.ljr.controller;

import com.ljr.annotation.NoToken;
import com.ljr.common.R;
import com.ljr.common.ResultCode;
import com.ljr.dto.LoginDTO;
import com.ljr.entity.SysUser;
import com.ljr.service.ISysUserService;
import com.ljr.util.JwtUtil;
import com.ljr.vo.LoginVO;
import com.ljr.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ISysUserService sysUserService;
    private final JwtUtil jwtUtil;

    @NoToken
    @PostMapping("/login")
    public R<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        SysUser user = sysUserService.login(loginDTO.getUsername(), loginDTO.getPassword());

        if (user != null) {
            // 生成JWT token
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());

            // 转换为VO对象
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);

            // 构建登录返回信息
            LoginVO loginVO = new LoginVO()
                    .setToken(token)
                    .setUser(userVO);

            return R.ok(loginVO);
        }
        return R.fail(ResultCode.LOGIN_ERROR);
    }

    @PostMapping("/logout")
    public R<Void> logout(@RequestHeader("Authorization") String token) {
        // 实际项目中可以将token加入黑名单
        return R.ok();
    }
} 