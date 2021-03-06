package com.cool.mmc.manager.service.impl;

import com.cool.mmc.manager.mapper.PayRecordMapper;
import com.cool.mmc.manager.entity.PayRecord;
import com.cool.mmc.manager.service.PayRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.core.common.Cools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("payRecordService")
public class PayRecordServiceImpl extends ServiceImpl<PayRecordMapper, PayRecord> implements PayRecordService {

    @Autowired
    private PayRecordMapper payRecordMapper;

    @Override
    public Integer selectOrderCount(Long userId) {
        if (Cools.isEmpty(userId)) {
            return payRecordMapper.selectOrderCount();
        } else {
            return payRecordMapper.selectOrderCountByUser(userId);
        }
    }

    @Override
    public Integer selectOrderCountByCurrentWeek(Long userId) {
        if (Cools.isEmpty(userId)) {
            return payRecordMapper.selectOrderCountByCurrentWeek();
        } else {
            return payRecordMapper.selectOrderCountByCurrentWeekAndUser(userId);
        }
    }

    @Override
    public Double selectMoneyByCurrentYear(Long userId) {
        if (Cools.isEmpty(userId)) {
            return payRecordMapper.selectCountByCurrentYear();
        } else {
            return payRecordMapper.selectCountByCurrentYearAndUser(userId);
        }
    }

    @Override
    public Double selectMoney(Long userId) {
        if (Cools.isEmpty(userId)) {
            return payRecordMapper.selectMoney();
        } else {
            return payRecordMapper.selectMoneyByUser(userId);
        }
    }

    @Override
    public List<Map<String, Object>> getReport(Long userId, Integer year, Integer month) {
        if (Cools.isEmpty(month)) {
            if (Cools.isEmpty(userId)){
                return payRecordMapper.getReportByYear(year);
            } else {
                return payRecordMapper.getReportByYearAndUser(userId, year);
            }
        } else {
            if (Cools.isEmpty(userId)){
                return payRecordMapper.getReportByMonth(year, month);
            } else {
                return payRecordMapper.getReportByMonthAndUser(userId, year, month);
            }
        }
    }
}
