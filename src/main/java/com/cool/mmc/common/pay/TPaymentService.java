package com.cool.mmc.common.pay;

import com.cool.mmc.common.entity.IWxPayConfig;

public interface TPaymentService {

    String getCodeUrl();

    Object codeUrlNotify(Object notifyData);

    Object getAuth(IWxPayConfig payConfig, String outTradeNo, Double money, String productId, String clientIp, String openId);

    boolean asyncNotify(Object notifyData);

}
