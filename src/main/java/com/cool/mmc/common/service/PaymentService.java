package com.cool.mmc.common.service;

import com.cool.mmc.common.entity.enums.PayCompanyType;
import com.cool.mmc.common.utils.PayUtils;
import com.cool.mmc.common.pay.TPaymentService;
import com.core.common.SpringUtils;
import org.springframework.stereotype.Service;

/**
 * <strong>支付体系</strong>
 * Created by vincent on 2019-04-19
 */
@Service
public class PaymentService {

    public static PaymentService getBean(){
        return SpringUtils.getBean(PaymentService.class);
    }

    public Object executePayMoney(PayCompanyType company, Long userId, String orderId, Double money, String clientIp, String openId, String productId) {
        TPaymentService service = PayUtils.getPaymentService(company);
        return service.getAuth(orderId, money, productId, clientIp, openId);
    }

    public boolean executePayMoneyNotify(Object notifyData, PayCompanyType company) {
        TPaymentService service = PayUtils.getPaymentService(company);
        return service.asyncNotify(notifyData);
    }

    public synchronized void executePaySuccess(String out_trade_no, String transaction_id, String buyer_email) {
        // todo 业务处理
    }

}
