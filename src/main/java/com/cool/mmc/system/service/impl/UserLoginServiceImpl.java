package com.cool.mmc.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cool.mmc.system.entity.UserLogin;
import com.cool.mmc.system.mapper.UserLoginMapper;
import com.cool.mmc.system.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userLoginService")
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements UserLoginService {

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Override
    public int selectCountByCurrentWeek() {
        return userLoginMapper.selectCountByCurrentWeek();
    }
}
