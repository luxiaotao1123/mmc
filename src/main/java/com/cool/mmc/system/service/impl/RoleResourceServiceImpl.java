package com.cool.mmc.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cool.mmc.system.entity.RoleResource;
import com.cool.mmc.system.mapper.RoleResourceMapper;
import com.cool.mmc.system.service.RoleResourceService;
import org.springframework.stereotype.Service;

@Service("roleResourceService")
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {

}
