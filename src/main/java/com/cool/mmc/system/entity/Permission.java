package com.cool.mmc.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cool.mmc.system.service.ResourceService;
import com.core.common.Cools;
import com.core.common.SpringUtils;

import java.io.Serializable;

@TableName("sys_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String action;

    /**
     * 所属菜单
     */
    @TableField("resource_id")
    private Long resourceId;

    /**
     * 状态 1: 正常  0: 禁用  
     */
    private Short status;

    public Permission() {}

    public Permission(String name,String action,Long resourceId,Short status) {
        this.name = name;
        this.action = action;
        this.resourceId = resourceId;
        this.status = status;
    }

//    Permission permission = new Permission(
//            null,    // 权限名称[非空]
//            null,    // 接口地址[非空]
//            null,    // 所属菜单
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
