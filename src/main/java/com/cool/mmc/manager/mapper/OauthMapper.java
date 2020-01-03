package com.cool.mmc.manager.mapper;

import com.cool.mmc.manager.entity.Oauth;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OauthMapper extends BaseMapper<Oauth> {

}
