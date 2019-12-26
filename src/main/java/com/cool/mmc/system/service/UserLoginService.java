package com.cool.mmc.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.cool.mmc.system.entity.UserLogin;

public interface UserLoginService extends IService<UserLogin> {

    int selectCountByCurrentWeek();

}
