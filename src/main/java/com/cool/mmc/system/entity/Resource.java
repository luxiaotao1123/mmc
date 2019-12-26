package com.cool.mmc.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cool.mmc.system.service.ResourceService;
import com.core.common.Cools;
import com.core.common.SpringUtils;

import java.io.Serializable;

@TableName("sys_resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单编码
     */
    private String code;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父级菜单
     */
    @TableField("resource_id")
    private Long resourceId;

    /**
     * 菜单等级 1: 一级菜单  2: 二级菜单  
     */
    private Short level;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态 1: 正常  0: 禁用  
     */
    private Short status;

    public Resource() {}

    public Resource(String code,String name,Long resourceId,Short level,Integer sort,Short status) {
        this.code = code;
        this.name = name;
        this.resourceId = resourceId;
        this.level = level;
        this.sort = sort;
        this.status = status;
    }

//    Resource resource = new Resource(
//            null,    // 菜单编码[非空]
//            null,    // 菜单名称[非空]
//            null,    // 父级菜单
//            null,    // 菜单等级[非空]
//            null,    // 排序
//            null    // 状态[非空]
//    );

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public String getResourceName(){
        ResourceService service = SpringUtils.getBean(ResourceService.class);
        Resource resource = service.selectById(this.resourceId);
        if (!Cools.isEmpty(resource)){
            return resource.getName();
        }
        return null;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Short getLevel() {
        return level;
    }

    public String getLevel$(){
        if (null == this.level){ return null; }
        switch (this.level){
            case 1:
                return "一级菜单";
            case 2:
                return "二级菜单";
            default:
                return String.valueOf(this.level);
        }
    }

    public void setLevel(Short level) {
        this.level = level;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
