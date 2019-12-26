package com.cool.mmc.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cool.mmc.system.entity.Role;
import com.cool.mmc.system.mapper.RoleMapper;
import com.cool.mmc.system.service.RoleService;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
