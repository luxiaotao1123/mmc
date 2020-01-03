package com.cool.mmc.manager.service.impl;

import com.cool.mmc.manager.mapper.OauthMapper;
import com.cool.mmc.manager.entity.Oauth;
import com.cool.mmc.manager.service.OauthService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("oauthService")
public class OauthServiceImpl extends ServiceImpl<OauthMapper, Oauth> implements OauthService {

}
