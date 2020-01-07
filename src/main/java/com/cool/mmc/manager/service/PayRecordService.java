package com.cool.mmc.manager.service;

import com.baomidou.mybatisplus.service.IService;
import com.cool.mmc.manager.entity.PayRecord;

import java.util.List;
import java.util.Map;

public interface PayRecordService extends IService<PayRecord> {

    Integer selectOrderCount(Long userId);

    Integer selectOrderCountByCurrentWeek(Long userId);

    Double selectMoneyByCurrentYear(Long userId);

    Double selectMoney(Long userId);

    List<Map<String, Object>> getReport(Long userId, Integer year, Integer month);

}
