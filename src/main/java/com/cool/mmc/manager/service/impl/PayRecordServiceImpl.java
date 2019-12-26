package com.cool.mmc.manager.service.impl;

import com.cool.mmc.manager.mapper.PayRecordMapper;
import com.cool.mmc.manager.entity.PayRecord;
import com.cool.mmc.manager.service.PayRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("payRecordService")
public class PayRecordServiceImpl extends ServiceImpl<PayRecordMapper, PayRecord> implements PayRecordService {

}
