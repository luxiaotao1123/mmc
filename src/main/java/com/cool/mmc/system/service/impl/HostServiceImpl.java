package com.cool.mmc.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cool.mmc.system.entity.Host;
import com.cool.mmc.system.mapper.HostMapper;
import com.cool.mmc.system.service.HostService;
import org.springframework.stereotype.Service;

@Service("hostService")
public class HostServiceImpl extends ServiceImpl<HostMapper, Host> implements HostService {

}
