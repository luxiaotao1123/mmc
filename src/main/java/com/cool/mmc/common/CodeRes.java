package com.cool.mmc.common;

import com.core.common.BaseRes;

public interface CodeRes extends BaseRes {

    String COMMON_00001 = "100001-参数为空";

    // user
    String USER_10001 = "20001-账号不存在";
    String USER_10002 = "20002-账号已被禁用";
    String USER_10003 = "30003-密码错误";

    // pay
    String PAY_20001 = "30001-创建支付宝交易串异常";
}
