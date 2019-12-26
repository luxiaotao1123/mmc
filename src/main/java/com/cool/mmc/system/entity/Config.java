package com.cool.mmc.system.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("sys_config")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 对应值
     */
    private String value;

    /**
     * 类型 1: String  2: JSON  
     */
    private Short type;

    /**
     * 状态 1: 正常  0: 禁用  
     */
    private Short status;

    public Config() {}

    public Config(String name,String code,String value,Short type,Short status) {
        this.name = name;
        this.code = code;
        this.value = value;
        this.type = type;
        this.status = status;
    }

//    Config config = new Config(
//            null,    // 名称[非空]
//            null,    // 编码[非空]
//            null,    // 对应值[非空]
//            null,    // 类型[非空]
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Short getType() {
        return type;
    }

    public String getType$(){
        if (null == this.type){ return null; }
        switch (this.type){
            case 1:
                return "String";
            case 2:
                return "JSON";
            default:
                return String.valueOf(this.type);
        }
    }

    public void setType(Short type) {
        this.type = type;
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
