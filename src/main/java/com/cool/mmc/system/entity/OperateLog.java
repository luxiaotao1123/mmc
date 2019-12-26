package com.cool.mmc.system.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.cool.mmc.system.service.UserService;
import com.core.common.Cools;
import com.core.common.SpringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@TableName("sys_operate_log")
public class OperateLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 访问地址
     */
    private String action;

    /**
     * 用户
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 客户端IP
     */
    private String ip;

    /**
     * 请求数据
     */
    private String request;

    /**
     * 响应数据
     */
    private String response;

    /**
     * 添加时间
     */
    @TableField("create_time")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserUsername(){
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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


}
