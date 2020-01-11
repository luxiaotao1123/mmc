package com.cool.mmc.manager.entity;

import com.cool.mmc.system.entity.User;
import com.cool.mmc.system.service.UserService;
import com.core.common.Cools;import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.core.common.SpringUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

@TableName("man_oauth")
public class Oauth implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 授权Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属会员
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 账户
     */
    private String account;

    /**
     * 密钥
     */
    private String sign;

    /**
     * 回调地址
     */
    @TableField("callback_url")
    private String callbackUrl;

    /**
     * 会员划算比例
     */
    private Double ratio;

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
     * 状态 1: 正常  0: 禁用  
     */
    private Short status;

    public Oauth() {}

    public Oauth(Long userId,String account,String sign,String callbackUrl,Double ratio,Date createTime,Date updateTime,Short status) {
        this.userId = userId;
        this.account = account;
        this.sign = sign;
        this.callbackUrl = callbackUrl;
        this.ratio = ratio;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
    }

//    Oauth oauth = new Oauth(
//            null,    // 所属会员[非空]
//            null,    // 账户[非空]
//            null,    // 密钥[非空]
//            null,    // 回调地址
//            null,    // 会员划算比例
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

    public Long getUserId() {
        return userId;
    }

    public String getUserId$(){
        UserService service = SpringUtils.getBean(UserService.class);
        User user = service.selectById(this.userId);
        if (!Cools.isEmpty(user)){
            return user.getUsername();
        }
        return null;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
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
                return "正常";
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
