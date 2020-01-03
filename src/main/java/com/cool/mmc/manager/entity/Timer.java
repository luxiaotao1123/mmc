package com.cool.mmc.manager.entity;

import com.core.common.Cools;import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

@TableName("man_timer")
public class Timer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 地址
     */
    private String url;

    /**
     * 数据
     */
    private String data;

    /**
     * 次数
     */
    private Integer count;

    /**
     * 添加时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 状态 0: 启动  1: 停止  
     */
    private Integer status;

    public Timer() {}

    public Timer(String url,String data,Integer count,Date createTime,Date updateTime,Integer status) {
        this.url = url;
        this.data = data;
        this.count = count;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
    }

//    Timer timer = new Timer(
//            null,    // 地址[非空]
//            null,    // 数据
//            null,    // 次数
//            null,    // 添加时间[非空]
//            null,    // 修改时间
//            null    // 状态[非空]
//    );

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getCreateTime$(){
        if (Cools.isEmpty(this.createTime)){
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public String getUpdateTime$(){
        if (Cools.isEmpty(this.updateTime)){
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.updateTime);
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatus$(){
        if (null == this.status){ return null; }
        switch (this.status){
            case 0:
                return "启动";
            case 1:
                return "停止";
            default:
                return String.valueOf(this.status);
        }
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
