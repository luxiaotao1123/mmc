package com.cool.mmc.common.entity;

public interface IWxPayConfig {

	/**
	 * 获取用于加密的密钥
	 */
	public String getPrivateKey();

	/**
	 * 获取商户应用appid
	 */
	public String getAppId();

	/**
	 * 获取商户mchid
	 */
	public String getMchId();

	/**
	 * 获取异步通知URL
	 */
	public String getNotifyUrl();

	/**
	 * 获取证书路径
	 */
	public String getCertPath();

	/**
	 * 获取证书密码
	 */
	public String getCertPwd();

	/**
	 * 交易描述
	 */
	public String getSubject();
}
