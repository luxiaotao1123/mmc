package com.cool.mmc.manager.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cool.mmc.system.entity.User;
import com.cool.mmc.system.service.UserService;
import com.core.common.Cools;
import com.core.common.SpringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@TableName("man_merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用ID
     */
    @TableField("app_id")
    private String appId;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 商户号
     */
    private String partner;

    /**
     * 标题
     */
    private String subject;

    /**
     * 编码方式 1: utf-8  2: gbk  
     */
    private Short charset;

    /**
     * 加密方式 1: RSA2  2: AES  
     */
    @TableField("sign_type")
    private Short signType;

    /**
     * 私钥
     */
    @TableField("private_key")
    private String privateKey;

    /**
     * 公钥
     */
    @TableField("public_key")
    private String publicKey;

    /**
     * 权重值
     */
    private Integer weight;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String memo;

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
     * 操作人
     */
    private Long editor;

    /**
     * 状态 1: 正常  0: 禁用  
     */
    private Short status;

    public Merchant() {}

    public Merchant(String appId,String name,String partner,String subject,Short charset,Short signType,String privateKey,String publicKey,Integer weight,Integer sort,String memo,Date createTime,Date updateTime,Long editor,Short status) {
        this.appId = appId;
        this.name = name;
        this.partner = partner;
        this.subject = subject;
        this.charset = charset;
        this.signType = signType;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.weight = weight;
        this.sort = sort;
        this.memo = memo;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.editor = editor;
        this.status = status;
    }

//    Merchant merchant = new Merchant(
//            null,    // 应用ID[非空]
//            null,    // 商户名称[非空]
//            null,    // 商户号[非空]
//            null,    // 标题[非空]
//            null,    // 编码方式[非空]
//            null,    // 加密方式
//            null,    // 私钥[非空]
//            null,    // 公钥
//            null,    // 权重值
//            null,    // 排序
//            null,    // 备注
//            null,    // 添加时间[非空]
//            null,    // 修改时间
//            null,    // 操作人
//            null    // 状态[非空]
//    );

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Short getCharset() {
        return charset;
    }

    public String getCharset$(){
        if (null == this.charset){ return null; }
        switch (this.charset){
            case 1:
                return "utf-8";
            case 2:
                return "gbk";
            default:
                return String.valueOf(this.charset);
        }
    }

    public void setCharset(Short charset) {
        this.charset = charset;
    }

    public Short getSignType() {
        return signType;
    }

    public String getSignType$(){
        if (null == this.signType){ return null; }
        switch (this.signType){
            case 1:
                return "RSA2";
            case 2:
                return "AES";
            default:
                return String.valueOf(this.signType);
        }
    }

    public void setSignType(Short signType) {
        this.signType = signType;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public Long getEditor() {
        return editor;
    }

    public String getEditor$(){
        UserService service = SpringUtils.getBean(UserService.class);
        User user = service.selectById(this.editor);
        if (!Cools.isEmpty(user)){
            return user.getUsername();
        }
        return null;
    }

    public void setEditor(Long editor) {
        this.editor = editor;
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
