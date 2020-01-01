package com.cool.mmc.common.entity.enums;

/**
 * Created by vincent on 2019-04-19
 */
public enum PayCompanyType {

    aliPay(1,"支付宝"),
    wxH5(2,"微信H5支付"),
    wxNative(3, "微信扫码支付"),
    ;

    private String description;
    private int code;
    PayCompanyType(int code, String description){
        this.code = code;
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }
}
