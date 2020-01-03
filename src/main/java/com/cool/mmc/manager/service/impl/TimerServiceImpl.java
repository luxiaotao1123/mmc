package com.cool.mmc.manager.service.impl;

import com.cool.mmc.manager.mapper.TimerMapper;
import com.cool.mmc.manager.entity.Timer;
import com.cool.mmc.manager.service.TimerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("timerService")
public class TimerServiceImpl extends ServiceImpl<TimerMapper, Timer> implements TimerService {

}
