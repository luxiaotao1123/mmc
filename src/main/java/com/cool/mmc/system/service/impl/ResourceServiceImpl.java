package com.cool.mmc.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cool.mmc.system.entity.Resource;
import com.cool.mmc.system.mapper.ResourceMapper;
import com.cool.mmc.system.service.ResourceService;
import org.springframework.stereotype.Service;

@Service("resourceService")
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

}
