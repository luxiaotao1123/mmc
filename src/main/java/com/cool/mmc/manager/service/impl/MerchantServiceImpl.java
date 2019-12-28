package com.cool.mmc.manager.service.impl;

import com.cool.mmc.manager.mapper.MerchantMapper;
import com.cool.mmc.manager.entity.Merchant;
import com.cool.mmc.manager.service.MerchantService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("merchantService")
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

}
