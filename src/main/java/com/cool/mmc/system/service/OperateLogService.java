package com.cool.mmc.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.cool.mmc.system.entity.OperateLog;

import java.util.List;
import java.util.Map;

public interface OperateLogService extends IService<OperateLog> {

    int selectCountByCurrentWeek();

    List<Map<String, Object>> getReport(Integer year, Integer month);

}
