package com.huodan.controller;

import com.huodan.entity.SysUser;
import com.huodan.model.LoginRequest;
import com.huodan.model.LoginResponse;
import com.huodan.common.Result;
import com.huodan.service.SysUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final SysUserService userService;

    public LoginController(SysUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        SysUser user = userService.login(request.getUsername(), request.getPassword());
        
        if (user != null) {
            LoginResponse response = new LoginResponse(
                UUID.randomUUID().toString(),  // token
                user.getUsername()             // username
            );
            return new Result<>(true, "登录成功", response);
        } else {
            return new Result<>(false, "用户名或密码错误", null);
        }
    }
} 