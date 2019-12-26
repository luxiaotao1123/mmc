package com.cool.mmc.common;

import com.core.common.BaseRes;

public interface CodeRes extends BaseRes {

    String COMMON_00001 = "00001-参数为空";

    // user
    String USER_10001 = "10001-账号不存在";
    String USER_10002 = "10002-账号已被禁用";
    String USER_10003 = "10003-密码错误";

    // pay
    String PAY_20001 = "20001-创建支付宝交易串异常";
}
