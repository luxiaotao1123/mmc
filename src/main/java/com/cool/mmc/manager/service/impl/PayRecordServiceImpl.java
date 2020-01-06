package com.cool.mmc.manager.service.impl;

import com.cool.mmc.manager.mapper.PayRecordMapper;
import com.cool.mmc.manager.entity.PayRecord;
import com.cool.mmc.manager.service.PayRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.core.common.Cools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("payRecordService")
public class PayRecordServiceImpl extends ServiceImpl<PayRecordMapper, PayRecord> implements PayRecordService {

    @Autowired
    private PayRecordMapper payRecordMapper;

    @Override
    public Double selectCountByCurrentYear(Long userId) {
        if (Cools.isEmpty(userId)) {
            return payRecordMapper.selectCountByCurrentYear();
        } else {
            return payRecordMapper.selectCountByCurrentYear(userId);
        }
    }

    @Override
    public Double selectCount(Long userId) {
        if (Cools.isEmpty(userId)) {
            return payRecordMapper.selectMoney();
        } else {
            return payRecordMapper.selectMoney(userId);
        }
    }
}
