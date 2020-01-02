package com.cool.mmc.common.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.common.CodeRes;
import com.cool.mmc.common.entity.PayConfig;
import com.cool.mmc.common.entity.enums.PayCompanyType;
import com.cool.mmc.common.utils.PayUtils;
import com.cool.mmc.common.pay.TPaymentService;
import com.cool.mmc.manager.entity.Merchant;
import com.cool.mmc.manager.entity.Product;
import com.cool.mmc.manager.service.MerchantService;
import com.cool.mmc.manager.service.ProductService;
import com.core.common.Cools;
import com.core.common.SpringUtils;
import com.core.exception.CoolException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private ProductService productService;

    public Object executePayMoney(PayCompanyType company, Long userId, String orderId, Double money, String clientIp, String openId, String productId) {
        Product product = productService.selectOne(new EntityWrapper<Product>().eq("flag", company.getFlag()));
        if (Cools.isEmpty(product)) {
            throw new CoolException(CodeRes.EMPTY);
        }
        Merchant merchant = merchantService.poll(product.getId());
        PayConfig payConfig = new PayConfig(merchant.getPrivateKey(), merchant.getAppId(), merchant.getPartner(), null, null, null, merchant.getSubject());
        TPaymentService service = PayUtils.getPaymentService(company);
        return service.getAuth(payConfig, orderId, money, productId, clientIp, openId);
    }

    public boolean executePayMoneyNotify(Object notifyData, PayCompanyType company) {
        TPaymentService service = PayUtils.getPaymentService(company);
        return service.asyncNotify(notifyData);
    }

    public synchronized void executePaySuccess(String out_trade_no, String transaction_id, String buyer_email) {
        // todo 业务处理
    }

}
