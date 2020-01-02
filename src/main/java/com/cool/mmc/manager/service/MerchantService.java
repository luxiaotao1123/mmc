package com.cool.mmc.manager.service;

import com.cool.mmc.manager.entity.Merchant;
import com.baomidou.mybatisplus.service.IService;

public interface MerchantService extends IService<Merchant> {

    Merchant poll(Long productId);

}
