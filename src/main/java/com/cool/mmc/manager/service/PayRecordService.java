package com.cool.mmc.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.cool.mmc.manager.entity.PayRecord;

public interface PayRecordService extends IService<PayRecord> {

    Double selectCountByCurrentYear(Long userId);

    Double selectCount(Long userId);

}
