package com.cool.mmc.manager.service.impl;

import com.cool.mmc.manager.mapper.GatewayLogMapper;
import com.cool.mmc.manager.entity.GatewayLog;
import com.cool.mmc.manager.service.GatewayLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("gatewayLogService")
public class GatewayLogServiceImpl extends ServiceImpl<GatewayLogMapper, GatewayLog> implements GatewayLogService {

}
