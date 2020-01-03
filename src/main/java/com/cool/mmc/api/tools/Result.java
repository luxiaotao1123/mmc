package com.cool.mmc.api.tools;

import java.util.Map;

/**
 * 应答格式
 * data 数据
 * message 描述
 * */
public class Result{
    private Map<String,Object> data;
    private String message;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
