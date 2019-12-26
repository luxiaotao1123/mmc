package com.cool.mmc.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cool.mmc.system.entity.OperateLog;
import com.cool.mmc.system.mapper.OperateLogMapper;
import com.cool.mmc.system.service.OperateLogService;
import com.core.common.Cools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("operateLogService")
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;


    @Override
    public int selectCountByCurrentWeek() {
        return operateLogMapper.selectCountByCurrentWeek();
    }

    @Override
    public List<Map<String, Object>> getReport(Integer year, Integer month) {
        if (Cools.isEmpty(month)) {
            return operateLogMapper.getReportByYear(year);
        }
        return operateLogMapper.getReportByMonth(year, month);
    }

}
