package com.huodan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huodan.entity.SysUser;
import com.huodan.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

@Service
public class SysUserService {
    private final SysUserMapper userMapper;

    public SysUserService(SysUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public SysUser login(String username, String password) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                   .eq("password", password);
        return userMapper.selectOne(queryWrapper);
    }
} 