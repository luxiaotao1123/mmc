package com.cool.mmc.manager.entity;

import com.core.common.Cools;import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

@TableName("man_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 标识
     */
    private String flag;

    /**
     * 所属平台 1: 微信  2: 支付宝  
     */
    private Short platform;

    /**
     * 状态 1: 正常  0: 禁用  
     */
    private Short status;

    public Product() {}

    public Product(String name,String flag,Short platform,Short status) {
        this.name = name;
        this.flag = flag;
        this.platform = platform;
        this.status = status;
    }

//    Product product = new Product(
//            null,    // 接口名称[非空]
//            null,    // 标识[非空]
//            null,    // 所属平台
//            null    // 状态[非空]
//    );

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Short getPlatform() {
        return platform;
    }

    public String getPlatform$(){
        if (null == this.platform){ return null; }
        switch (this.platform){
            case 1:
                return "微信";
            case 2:
                return "支付宝";
            default:
                return String.valueOf(this.platform);
        }
    }

    public void setPlatform(Short platform) {
        this.platform = platform;
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
