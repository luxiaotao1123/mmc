package com.cool.mmc.common.pay.impl;

import com.cool.mmc.common.CodeRes;
import com.cool.mmc.common.pay.WxPaymentServiceSupport;
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



            return null;
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
