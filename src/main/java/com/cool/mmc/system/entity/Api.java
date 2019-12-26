package com.cool.mmc.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.core.common.Cools;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@TableName("sys_api")
public class Api implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 授权 0: 无需授权  1: 需要授权  
     */
    private Short oauth;

    /**
     * 请求结构
     */
    private String request;

    /**
     * 响应结构
     */
    private String response;

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
     * 状态 1: 有效  0: 禁用  
     */
    private Short status;

    public Api() {}

    public Api(String namespace,Short oauth,String request,String response,Date createTime,Date updateTime,Short status) {
        this.namespace = namespace;   // 命名空间[非空]
        this.oauth = oauth;   // 授权[非空]
        this.request = request;   // 请求结构
        this.response = response;   // 响应结构
        this.createTime = createTime;   // 添加时间[非空]
        this.updateTime = updateTime;   // 修改时间
        this.status = status;   // 状态[非空]
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Short getOauth() {
        return oauth;
    }

    public String getOauth$(){
        if (null == this.oauth){ return null; }
        switch (this.oauth){
            case 0:
                return "无需授权";
            case 1:
                return "需要授权";
            default:
                return String.valueOf(this.oauth);
        }
    }

    public void setOauth(Short oauth) {
        this.oauth = oauth;
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

    public Short getStatus() {
        return status;
    }

    public String getStatus$(){
        if (null == this.status){ return null; }
        switch (this.status){
            case 1:
                return "有效";
            case 0:
                return "禁用";
            default:
                return String.valueOf(this.status);
        }
    }

    public void setStatus(Short status) {
        this.status = status;
    }


}
