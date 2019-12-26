package com.cool.mmc.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cool.mmc.system.entity.Api;
import com.cool.mmc.system.mapper.ApiMapper;
import com.cool.mmc.system.service.ApiService;
import org.springframework.stereotype.Service;

@Service("apiService")
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Api> implements ApiService {

}
