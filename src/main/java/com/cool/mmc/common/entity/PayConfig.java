package com.cool.mmc.common.entity;

/**
 * Created by vincent on 2020-01-02
 */
public class PayConfig implements IWxPayConfig {

    private String privateKey;

    private String appId;

    private String mchId;

    private String notifyUrl;

    private String certPath;

    private String certPwd;

    private String subject;

    public PayConfig() {
    }

    public PayConfig(String privateKey, String appId, String mchId, String notifyUrl, String certPath, String certPwd, String subject) {
        this.privateKey = privateKey;
        this.appId = appId;
        this.mchId = mchId;
        this.notifyUrl = notifyUrl;
        this.certPath = certPath;
        this.certPwd = certPwd;
        this.subject = subject;
    }

    @Override
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    @Override
    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    @Override
    public String getCertPwd() {
        return certPwd;
    }

    public void setCertPwd(String certPwd) {
        this.certPwd = certPwd;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
