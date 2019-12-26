package com.cool.mmc.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cool.mmc.system.entity.RolePermission;
import com.cool.mmc.system.mapper.RolePermissionMapper;
import com.cool.mmc.system.service.RolePermissionService;
import org.springframework.stereotype.Service;

@Service("rolePermissionService")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

}
