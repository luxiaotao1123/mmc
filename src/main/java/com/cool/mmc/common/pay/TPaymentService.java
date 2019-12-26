package com.cool.mmc.common.pay;

public interface TPaymentService {

    Object getAuth(String outTradeNo, Double money, String clientIp, String openId);

    boolean asyncNotify(Object notifyData);

}
