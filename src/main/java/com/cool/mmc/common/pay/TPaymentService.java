package com.cool.mmc.common.pay;

public interface TPaymentService {

    String getCodeUrl();

    Object codeUrlNotify(Object notifyData);

    Object getAuth(String outTradeNo, Double money, String productId, String clientIp, String openId);

    boolean asyncNotify(Object notifyData);

}
