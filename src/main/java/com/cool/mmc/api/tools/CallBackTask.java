package com.cool.mmc.api.tools;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.manager.entity.PayRecord;
import com.cool.mmc.manager.entity.Timer;
import com.cool.mmc.manager.service.PayRecordService;
import com.cool.mmc.manager.service.TimerService;
import com.core.common.Cools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


/**
 * 定时器
 * 定时向客户端发送回调请求
 * */
@EnableScheduling
@Component
public class CallBackTask {

    @Autowired
    private TimerService timerService;

    @Autowired
    private PayRecordService payRecordService;

    @Scheduled(fixedRate=5000)
    public void callback() {
        List<Timer> timers = timerService.selectList(new EntityWrapper<Timer>().eq("status", 0));
        for(Timer timer:timers){
            try {
                String post = HttpSend.doPost(timer.getUrl(), JSONObject.parseObject(timer.getData()));
                JSONObject jsonObject = JSONObject.parseObject(post);
                if (!Cools.isEmpty(jsonObject.getString("code"))) {
                    if (jsonObject.getString("code").equals("200")) {
                        PayRecord payRecord = payRecordService.selectById(timer.getPayRecordId());
                        if (payRecord != null) {
                            payRecord.setState((short) 3);
                            payRecordService.updateById(payRecord);
                        }
                        timer.setStatus(1);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println(timer.getCount()+1+"循环回答"+timer.getUrl());
            timer.setUpdateTime(new Date());
            timer.setCount(timer.getCount()+1);
            timerService.updateById(timer);
        }

    }

}
