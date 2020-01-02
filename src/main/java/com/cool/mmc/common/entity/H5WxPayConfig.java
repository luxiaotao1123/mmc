package com.cool.mmc.common.entity;

/**
 * Created by vincent on 2019-12-30
 */
public class H5WxPayConfig implements IWxPayConfig {

    @Override
    public String getPrivateKey() {
        return "dbc5cf176c48a4d763bbd8eb3e010985";
    }

    @Override
    public String getAppId() {
        return "wx773e38b57a7c1687";
    }

    @Override
    public String getMchId() {
        return "1533158701";
    }

    @Override
    public String getNotifyUrl() {
        return "http://zg.hzddbb.com/callback/v1/h5";
    }

    @Override
    public String getCertPath() {
        return null;
    }

    @Override
    public String getCertPwd() {
        return null;
    }

    @Override
    public String getSubject() {
        return null;
    }
}
