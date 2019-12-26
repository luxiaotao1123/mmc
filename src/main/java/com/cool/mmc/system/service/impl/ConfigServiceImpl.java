package com.cool.mmc.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cool.mmc.system.entity.Config;
import com.cool.mmc.system.mapper.ConfigMapper;
import com.cool.mmc.system.service.ConfigService;
import org.springframework.stereotype.Service;

@Service("configService")
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

}
