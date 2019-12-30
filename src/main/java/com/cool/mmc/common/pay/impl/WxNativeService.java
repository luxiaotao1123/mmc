package com.cool.mmc.common.pay.impl;

import com.cool.mmc.common.CodeRes;
import com.cool.mmc.common.entity.IWxPayConfig;
import com.cool.mmc.common.entity.NativeWxPayConfig;
import com.cool.mmc.common.entity.WxPayData;
import com.cool.mmc.common.pay.WxPaymentServiceSupport;
import com.cool.mmc.common.utils.HttpTools;
import com.cool.mmc.manager.entity.Merchant;
import com.core.common.Arith;
import com.core.exception.CoolException;
import org.springframework.stereotype.Service;

/**
 * Created by vincent on 2019-12-30
 */
@Service("wxNativeService")
public class WxNativeService extends WxPaymentServiceSupport {

    @Override
    public String getCodeUrl() {
        WxPayData payData = new WxPayData();
        payData.setValue("time_stamp", String.valueOf(System.currentTimeMillis()/1000));
        payData.setValue("nonce_str", createNonceStr());
        payData.setValue("product_id", "88888");
        payData.setValues(new NativeWxPayConfig());
        String sign = payData.makeSign();
        return "";
    }

    @Override
    public Object codeUrlNotify(Object notifyData) {

        return false;
    }

    @Override
    public Object getAuth(String outTradeNo, Double money, String productId, String clientIp, String openId) {
        try {
            if(null == clientIp || !HttpTools.isboolIp(clientIp)){
                clientIp = "127.0.0.1";
            }
            Merchant merchant = new Merchant();
            merchant.setSubject("递递叭叭测试");
            IWxPayConfig wxPayConfig = new NativeWxPayConfig();
            int totalFee = (int) Arith.multiplys(0, money, 100);
            String result = getNativeUnifiedOrderResult(
                    totalFee
                    , merchant.getSubject()
                    , clientIp
                    , outTradeNo
                    , wxPayConfig
                    , productId);

            WxPayData res = new WxPayData();
            res.setValues(wxPayConfig);
            res.fromXml(result);

            return res.getValue("mweb_url");
        } catch (Exception e) {
            e.printStackTrace();
            throw new CoolException(CodeRes.ERROR);
        }
    }

    @Override
    public boolean asyncNotify(Object notifyData) {
        return false;
    }

    public static void main(String[] args) {
        IWxPayConfig wxPayConfig = new NativeWxPayConfig();
        WxPayData payData = new WxPayData();
//        payData.setValue("appid", wxPayConfig.getAppId());// 公众账号ID
//        payData.setValue("mch_id", wxPayConfig.getMchId());// 商户号
        payData.setValue("time_stamp", String.valueOf(System.currentTimeMillis()/1000));
//        payData.setValue("nonce_str", createNonceStr());
        payData.setValue("product_id", "88888");
        payData.setValues(wxPayConfig);
        String sign = payData.makeSign();

        String res = "weixin://wxpay/bizpayurl?sign=".concat(sign).concat("&appid=")
                .concat(wxPayConfig.getAppId()).concat("&mch_id=").concat(wxPayConfig.getMchId())
                .concat("&product_id=").concat("88888").concat("&time_stamp=").concat(String.valueOf(System.currentTimeMillis()/1000))
                .concat("&nonce_str=").concat(createNonceStr());
        System.out.println(res);
    }
}
