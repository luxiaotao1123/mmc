package com.cool.mmc.manager.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cool.mmc.manager.service.PayRecordService;
import com.core.common.Cools;
import com.core.common.SpringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@TableName("man_timer")
public class Timer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单
     */
    @TableField("pay_record_id")
    private Long payRecordId;

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

    public Timer(Long payRecordId,String url,String data,Integer count,Date createTime,Date updateTime,Integer status) {
        this.payRecordId = payRecordId;
        this.url = url;
        this.data = data;
        this.count = count;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
    }

//    Timer timer = new Timer(
//            null,    // 订单[非空]
//            null,    // 地址[非空]
//            null,    // 数据
//            null,    // 次数
//            null,    // 添加时间[非空]
//            null,    // 修改时间
//            null    // 状态[非空]
//    );

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPayRecordId() {
        return payRecordId;
    }

    public String getPayRecordId$(){
        PayRecordService service = SpringUtils.getBean(PayRecordService.class);
        PayRecord payRecord = service.selectById(this.payRecordId);
        if (!Cools.isEmpty(payRecord)){
            return String.valueOf(payRecord.getId());
        }
        return null;
    }

    public void setPayRecordId(Long payRecordId) {
        this.payRecordId = payRecordId;
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
