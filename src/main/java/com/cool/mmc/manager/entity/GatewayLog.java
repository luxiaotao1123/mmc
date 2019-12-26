package com.cool.mmc.manager.entity;

import com.core.common.Cools;import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.core.common.SpringUtils;
import com.cool.mmc.manager.service.MerchantService;
import com.baomidou.mybatisplus.annotations.TableField;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

@TableName("man_gateway_log")
public class GatewayLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属商户
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 访问地址
     */
    private String url;

    /**
     * 客户端地址
     */
    @TableField("client_ip")
    private String clientIp;

    /**
     * 请求内容
     */
    private String request;

    /**
     * 相应内容
     */
    private String response;

    /**
     * 添加时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 数据状态 1: 成功  2: 失败  
     */
    private Short status;

    public GatewayLog() {}

    public GatewayLog(Long merchantId,String url,String clientIp,String request,String response,Date createTime,Short status) {
        this.merchantId = merchantId;
        this.url = url;
        this.clientIp = clientIp;
        this.request = request;
        this.response = response;
        this.createTime = createTime;
        this.status = status;
    }

//    GatewayLog gatewayLog = new GatewayLog(
//            null,    // 所属商户[非空]
//            null,    // 访问地址[非空]
//            null,    // 客户端地址[非空]
//            null,    // 请求内容[非空]
//            null,    // 相应内容[非空]
//            null,    // 添加时间[非空]
//            null    // 数据状态[非空]
//    );

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public String getMerchantId$(){
        MerchantService service = SpringUtils.getBean(MerchantService.class);
        Merchant merchant = service.selectById(this.merchantId);
        if (!Cools.isEmpty(merchant)){
            return merchant.getName();
        }
        return null;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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

    public Short getStatus() {
        return status;
    }

    public String getStatus$(){
        if (null == this.status){ return null; }
        switch (this.status){
            case 1:
                return "成功";
            case 2:
                return "失败";
            default:
                return String.valueOf(this.status);
        }
    }

    public void setStatus(Short status) {
        this.status = status;
    }


}
