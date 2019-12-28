package com.cool.mmc.common.pay.impl;

import com.cool.mmc.common.CodeRes;
import com.cool.mmc.common.entity.IWxPayConfig;
import com.cool.mmc.common.entity.WxPayData;
import com.cool.mmc.common.pay.WxPaymentServiceSupport;
import com.cool.mmc.common.utils.HttpTools;
import com.cool.mmc.manager.entity.Merchant;
import com.core.common.Arith;
import com.core.exception.CoolException;
import org.springframework.stereotype.Service;

/**
 * 微信h5支付服务类
 * Created by vincent on 2019-12-28
 */
@Service("wxH5Service")
public class WxH5Service extends WxPaymentServiceSupport {


    @Override
    public Object getAuth(String outTradeNo, Double money, String clientIp, String openId) {
        try {
            if(null == clientIp || !HttpTools.isboolIp(clientIp)){
                clientIp = "127.0.0.1";
            }
            Merchant merchant = new Merchant();
            IWxPayConfig iWxPayConfig = new IWxPayConfig() {
                @Override
                public String getPrivateKey() {
                    return null;
                }

                @Override
                public String getAppId() {
                    return null;
                }

                @Override
                public String getMchId() {
                    return null;
                }

                @Override
                public String getNotifyUrl() {
                    return null;
                }

                @Override
                public String getCertPath() {
                    return null;
                }

                @Override
                public String getCertPwd() {
                    return null;
                }
            };
            int totalFee = (int) Arith.multiplys(0, money, 100);
            String result = getH5UnifiedOrderResult(
                    totalFee
                    , merchant.getSubject()
                    , clientIp
                    , outTradeNo
                    , iWxPayConfig);

            WxPayData res = new WxPayData();
            res.setValues(iWxPayConfig);
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


}
