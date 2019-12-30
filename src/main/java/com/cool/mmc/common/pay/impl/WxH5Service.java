package com.cool.mmc.common.pay.impl;

import com.cool.mmc.common.CodeRes;
import com.cool.mmc.common.entity.H5WxPayConfig;
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
    public Object getAuth(String outTradeNo, Double money, String productId, String clientIp, String openId) {
        try {
            if(null == clientIp || !HttpTools.isboolIp(clientIp)){
                clientIp = "127.0.0.1";
            }
            Merchant merchant = new Merchant();
            merchant.setSubject("递递叭叭测试");
            IWxPayConfig wxPayConfig = new H5WxPayConfig();
            int totalFee = (int) Arith.multiplys(0, money, 100);
            String result = getH5UnifiedOrderResult(
                    totalFee
                    , merchant.getSubject()
                    , clientIp
                    , outTradeNo
                    , wxPayConfig);

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


}
