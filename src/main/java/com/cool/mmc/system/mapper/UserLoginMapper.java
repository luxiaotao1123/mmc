package com.cool.mmc.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cool.mmc.system.entity.UserLogin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserLoginMapper extends BaseMapper<UserLogin> {

    @Select("select count(1) from sys_user_login where yearweek(date_format(create_time,'%Y-%m-%d')) = yearweek(now())")
    int selectCountByCurrentWeek();

}
