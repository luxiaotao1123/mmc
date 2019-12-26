package com.cool.mmc.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cool.mmc.system.service.PermissionService;
import com.cool.mmc.system.service.RoleService;
import com.core.common.Cools;
import com.core.common.SpringUtils;

import java.io.Serializable;

@TableName("sys_role_permission")
public class RolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 权限
     */
    @TableField("permission_id")
    private Long permissionId;

    public RolePermission() {}

    public RolePermission(Long roleId,Long permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

//    RolePermission rolePermission = new RolePermission(
//            null,    // 角色[非空]
//            null    // 权限[非空]
//    );

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName(){
        RoleService service = SpringUtils.getBean(RoleService.class);
        Role role = service.selectById(this.roleId);
        if (!Cools.isEmpty(role)){
            return role.getName();
        }
        return null;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public String getPermissionName(){
        PermissionService service = SpringUtils.getBean(PermissionService.class);
        Permission permission = service.selectById(this.permissionId);
        if (!Cools.isEmpty(permission)){
            return permission.getName();
        }
        return null;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }


}
