package com.cool.mmc.manager.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cool.mmc.manager.service.ProductService;
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
     * 所属会员
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 所属接口
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 商家姓名
     */
    private String name;

    /**
     * 应用ID
     */
    @TableField("app_id")
    private String appId;

    /**
     * 商户号
     */
    private String partner;

    /**
     * 标题
     */
    private String subject;

    /**
     * 密钥
     */
    @TableField("private_key")
    private String privateKey;

    /**
     * 公钥
     */
    @TableField("public_key")
    private String publicKey;

    /**
     * 证书路径
     */
    @TableField("cert_path")
    private String certPath;

    /**
     * 证书密码
     */
    @TableField("cert_pwd")
    private String certPwd;

    /**
     * 权重值
     */
    private Integer weight;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 轮回状态 1: 开启  0: 关闭  
     */
    private Short state;

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

    public Merchant(Long userId,Long productId,String name,String appId,String partner,String subject,String privateKey,String publicKey,String certPath,String certPwd,Integer weight,Integer sort,Short state,String memo,Date createTime,Date updateTime,Long editor,Short status) {
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.appId = appId;
        this.partner = partner;
        this.subject = subject;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.certPath = certPath;
        this.certPwd = certPwd;
        this.weight = weight;
        this.sort = sort;
        this.state = state;
        this.memo = memo;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.editor = editor;
        this.status = status;
    }

//    Merchant merchant = new Merchant(
//            null,    // 所属商户[非空]
//            null,    // 所属接口[非空]
//            null,    // 商家姓名[非空]
//            null,    // 应用ID[非空]
//            null,    // 商户号[非空]
//            null,    // 标题[非空]
//            null,    // 密钥[非空]
//            null,    // 公钥
//            null,    // 证书路径
//            null,    // 证书密码
//            null,    // 权重值
//            null,    // 排序
//            null,    // 轮回状态[非空]
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

    public Long getProductId() {
        return productId;
    }

    public String getProductId$(){
        ProductService service = SpringUtils.getBean(ProductService.class);
        Product product = service.selectById(this.productId);
        if (!Cools.isEmpty(product)){
            return product.getName();
        }
        return null;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getCertPwd() {
        return certPwd;
    }

    public void setCertPwd(String certPwd) {
        this.certPwd = certPwd;
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

    public Short getState() {
        return state;
    }

    public String getState$(){
        if (null == this.state){ return null; }
        switch (this.state){
            case 1:
                return "开启";
            case 0:
                return "关闭";
            default:
                return String.valueOf(this.state);
        }
    }

    public void setState(Short state) {
        this.state = state;
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
