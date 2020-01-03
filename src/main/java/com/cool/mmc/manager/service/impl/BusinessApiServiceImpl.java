package com.cool.mmc.manager.service.impl;

import com.cool.mmc.manager.mapper.BusinessApiMapper;
import com.cool.mmc.manager.entity.BusinessApi;
import com.cool.mmc.manager.service.BusinessApiService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("businessApiService")
public class BusinessApiServiceImpl extends ServiceImpl<BusinessApiMapper, BusinessApi> implements BusinessApiService {

}
