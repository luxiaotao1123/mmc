package com.cool.mmc.common.entity;

import com.alibaba.fastjson.JSON;
import com.cool.mmc.common.utils.Item;
import com.cool.mmc.common.utils.Xml;
import com.core.common.Cools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WxPayData implements Serializable {

	private static Logger log = LoggerFactory.getLogger(WxPayData.class);
	private static final long serialVersionUID = 2908927494985838824L;
	private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	private HashMap<String, Object> m_values = new HashMap<>();

	private IWxPayConfig wxPayConfig;

	/**
	 * 数据转出为xml格式
	 */
	public String toXml() throws Exception {
		if (0 == m_values.size()) {
			log.error("WxPayData数据为空");
			throw new Exception("WxPayData数据为空!");
		}
		StringBuilder xml = new StringBuilder("<xml>");
		for (String key : m_values.keySet()) {
			if (Cools.isEmpty(getValue(key))) {
				throw new Exception("WxPayData内部含有值为null或者为空的字段!");
			}
			if (getValue(key).getClass().getName().equals("java.lang.int") || getValue(key).getClass().getName().equals("java.lang.Integer")) {
				xml.append("<").append(key).append(">").append(getValue(key)).append("</").append(key).append(">");
			} else if (getValue(key).getClass().getName().equals("java.lang.String")) {
				xml.append("<").append(key).append(">").append("<![CDATA[").append(getValue(key)).append("]]></").append(key).append(">");
			} else {
				throw new Exception("WxPayData字段数据类型错误!");
			}
		}
		xml.append("</xml>");
		return xml.toString();
	}

	/**
	 * 解析xml转为内部变量
	 * wxPayConfig不能为空
	 */
	public void fromXml(String xml) throws Exception {
		if (Cools.isEmpty(xml)) {
			log.error("将空的xml串转换为WxPayData不合法!");
			throw new Exception("将空的xml串转换为WxPayData不合法!");
		}
		Xml requestXml = new Xml();
		if (xml.startsWith("<XML>") || xml.startsWith("<xml>")) {
			requestXml.Parse("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + xml);
		} else {
			requestXml.Parse(xml);
		}
		Item requestRoot = requestXml.getRoot();
		// 遍历XML节点，赋值给 m_values
		for (Item item : requestRoot.getItemList()) {
			if (item.getCData() == null) {
				m_values.put(item.getName(), item.getValue());
			} else {
				m_values.put(item.getName(), item.getCData());
			}
		}
		// 错误是没有签名
		if (getValue("return_code") != null && !getValue("return_code").equals("SUCCESS")) {
			return;
		}
		if (!checkSign()){
			throw new Exception("签名验证失败"+xml);
		}
	}

	/**
	 * 生成签名，详见签名生成算法
	 * wxPayConfig不能为空
	 */
	public String makeSign() {
		List<String> list = new ArrayList<>();
		for (String key : m_values.keySet()) {
			if (!key.equals("sign") && getValue(key) != null & getValue(key) != "") {
				list.add(key + "=" + getValue(key) + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + wxPayConfig.getPrivateKey();
		return md5(result).toUpperCase();
	}

	/**
	 * 检测签名是否正确
	 */
	public boolean checkSign() throws Exception {
		if(null == getValue("sign")  || "".equals(String.valueOf(getValue("sign")))){
			throw new Exception("WxPayData签名不存在!");
		}
		return makeSign().equals(getValue("sign").toString());
	}


	/**
	 *  格式转化成url参数格式 @ return url格式串, 该串不包含sign字段值
	 */
	@SuppressWarnings("unused")
	public String toUrl() throws Exception {
		StringBuilder buff = new StringBuilder();
		for (String key : m_values.keySet()) {
			if (getValue(key) == null) {
				log.error("WxPayData内部含有值为null的字段!");
				throw new Exception("WxPayData内部含有值为null的字段!");
			}

			if (!key.equals("sign") && getValue(key) != "") {
				buff.append(key).append("=").append(getValue(key)).append("&");
			}
		}
		if (buff.length() > 1)
			buff = new StringBuilder(buff.substring(0, buff.length() - 1));
		return buff.toString();
	}

	public static String md5(String string){
		try{
			MessageDigest md5=MessageDigest.getInstance("MD5");
			byte[] bytes=md5.digest(string.getBytes("utf-8"));
			char[] chars = new char[bytes.length * 2];
			for (int i = 0; i < bytes.length; i++) {
				int b = bytes[i];
				chars[i * 2] = hexDigits[(b & 0xF0) >> 4];
				chars[i * 2 + 1] = hexDigits[b & 0x0F];
			}
			return new String(chars);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	public void setValue(String key, Object value) {
		m_values.put(key, value);
	}

	public Object getValue(String key) {
		return m_values.get(key);
	}

	public HashMap<String, Object> values() {
		return m_values;
	}

	public void setValues(HashMap<String, Object> map) {
		m_values = map;
	}

	public void setValues(IWxPayConfig config) {
		wxPayConfig = config;
	}

	public boolean isSet(String key) {
		return null != m_values.get(key);
	}

	public String toJson() {
		return JSON.toJSONString(m_values);
	}

}
