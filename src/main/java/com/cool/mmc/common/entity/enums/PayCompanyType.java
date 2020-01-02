package com.cool.mmc.common.entity.enums;

/**
 * Created by vincent on 2019-04-19
 */
public enum PayCompanyType {

    aliPay(1, "ALIPAY", "支付宝"),
    wxH5(2, "WECHAT_H5", "微信H5支付"),
    wxNative(3, "WECHAT_NATIVE", "微信扫码支付"),
    ;

    private int code;
    private String flag;
    private String description;

    PayCompanyType(int code, String flag, String description){
        this.code = code;
        this.flag = flag;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
