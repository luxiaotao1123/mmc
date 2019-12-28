package com.cool.mmc.common.pay;

import com.cool.mmc.common.entity.IWxPayConfig;
import com.cool.mmc.common.entity.WxPayData;
import com.cool.mmc.common.entity.enums.WxPayType;
import com.core.common.DateUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 微信支付体系增强
 * Created by vincent on 2019-12-28
 */
public abstract class WxPaymentServiceSupport implements TPaymentService {

    // 统一下单API
    private static final String UNIFIED_ORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * h5
     */
    protected static String getH5UnifiedOrderResult(int totalFee, String body, String clientIp, String outTradeNo, IWxPayConfig wxPayConfig) throws Exception {
        WxPayData data = new WxPayData();
        data.setValue("body", body);
        data.setValue("total_fee", totalFee);
        data.setValue("device_info", "WEB");
        data.setValue("trade_type", WxPayType.MWEB.toString());
        data.setValue("spbill_create_ip", clientIp);
        // data.setValue("product_id", "123123");

        return getUnifiedOrderResult(data, outTradeNo, wxPayConfig);
    }

    /**
     * 统一下单
     */
    private static String getUnifiedOrderResult(WxPayData data, String outTradeNo, IWxPayConfig wxPayConfig) throws Exception {
        Date now = new Date();
        // 统一下单
        data.setValue("time_start", DateUtils.convert(now, DateUtils.yyyyMMddHHmmss));
        // 设置超时时间为20分钟
        data.setValue("time_expire", DateUtils.convert(DateUtils.calculate(now, 20L , TimeUnit.MINUTES), DateUtils.yyyyMMddHHmmss));

        data.setValue("goods_tag", "WXG");
        data.setValue("out_trade_no", outTradeNo);

        data.setValue("notify_url", wxPayConfig.getNotifyUrl());// 异步通知url
        data.setValue("appid", wxPayConfig.getAppId());// 公众账号ID
        data.setValue("mch_id", wxPayConfig.getMchId());// 商户号
        data.setValue("nonce_str", createNonceStr());// 随机字符串
        data.setValue("fee_type", "CNY");

        if (!data.isSet("out_trade_no")) {
            throw new Exception("缺少统一支付接口必填参数out_trade_no！");
        } else if (!data.isSet("body")) {
            throw new Exception("缺少统一支付接口必填参数body！");
        } else if (!data.isSet("total_fee")) {
            throw new Exception("缺少统一支付接口必填参数total_fee！");
        } else if (!data.isSet("trade_type")) {
            throw new Exception("缺少统一支付接口必填参数trade_type！");
        }

        // 关联参数
        if (data.getValue("trade_type").toString().equals("JSAPI") && !data.isSet("openid")) {
            throw new Exception("统一支付接口中，缺少必填参数openid！trade_type为JSAPI时，openid为必填参数！");
        }
        if (data.getValue("trade_type").toString().equals("NATIVE") && !data.isSet("product_id")) {
            throw new Exception("统一支付接口中，缺少必填参数product_id！trade_type为NATIVE时，product_id为必填参数！");
        }

        data.setValues(wxPayConfig);
        data.setValue("sign", data.makeSign());

        String xml = data.toXml();

        return post(UNIFIED_ORDER_API, xml, "utf-8");
    }

    // 私有方法 ----------------------------------------------------------------------------------------------------------

    /**
     * 随机字符串
     */
    private static String createNonceStr() {
        String strUUID= UUID.randomUUID().toString();
        return strUUID.replaceAll("-", "");
    }

    /**
     * 以post方式
     */
    public static String post(String action,String data,String encode){
        StringBuilder response = new StringBuilder();
        PostMethod method = new PostMethod(action);
        try{
            HttpClient client=new HttpClient();
            method.setRequestBody(data);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,encode);
            client.executeMethod(method);
            BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),encode));
            String line=null;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            reader.close();
            // }
        }catch(Exception | Error ex){
            ex.printStackTrace();
            return "{\"error\":1,\"message\":\"提交表单出现错误\"}";
        } finally{
            method.releaseConnection();
        }
        return response.toString();
    }


}


