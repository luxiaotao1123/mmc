package com.cool.mmc.common.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.system.entity.Config;
import com.cool.mmc.system.service.ConfigService;
import com.core.common.Cools;
import com.core.common.SpringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础配置中心。可通过刷新指定接口刷新相关配置
 */
public class Parameter {

    private volatile static Parameter instance = null;

    private Parameter(){
    }

    public static Parameter get(){
        if (instance == null){
            synchronized (Parameter.class){
                instance = reset();
                return instance;
            }
        }
        return instance;
    }

	/**
	 * 重置
	 */
	public static Parameter reset() {
        ConfigService configService = SpringUtils.getBean(ConfigService.class);
        List<Config> configs = configService.selectList(new EntityWrapper<Config>().eq("status", "1"));
        Map<String, Object> data = new HashMap<>();
        for (Config config : configs) {
            if (config.getType() == 1) {
                data.put(config.getCode(), String.valueOf(config.getValue()));
            } else {
                data.put(config.getCode(), JSON.parse(config.getValue()));
            }
        }
        instance = Cools.conver(data, Parameter.class);
        return instance;
    }

}
