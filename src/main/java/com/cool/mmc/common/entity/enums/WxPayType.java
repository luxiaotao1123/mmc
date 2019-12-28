package com.cool.mmc.common.entity.enums;

/**
 * 微信支付交易类型
 */
public enum WxPayType {

    MWEB, // H5支付
    JSAPI, // JSAPI支付（或小程序支付）
    APP, // app支付
    NATIVE, // Native支付
    ;
}
