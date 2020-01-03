package com.cool.mmc.common.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.common.CodeRes;
import com.cool.mmc.common.entity.PayConfig;
import com.cool.mmc.common.entity.enums.PayCompanyType;
import com.cool.mmc.common.pay.TPaymentService;
import com.cool.mmc.common.utils.PayUtils;
import com.cool.mmc.manager.entity.Merchant;
import com.cool.mmc.manager.entity.PayRecord;
import com.cool.mmc.manager.entity.Product;
import com.cool.mmc.manager.service.MerchantService;
import com.cool.mmc.manager.service.PayRecordService;
import com.cool.mmc.manager.service.ProductService;
import com.core.common.Cools;
import com.core.common.SpringUtils;
import com.core.exception.CoolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    @Autowired
    private PayRecordService payRecordService;

    /**
     * 发起支付，获取支付串
     * @param orderId 外部订单号
     * @param money 金额
     * @param clientIp 客户端ip
     * @param openId 微信openId
     * @param productId 微信二维码支付产品ID
     * @return the result
     */
    public Object executePayMoney(PayCompanyType company, String orderId, Double money, String clientIp, String openId, String productId) {
        Product product = productService.selectOne(new EntityWrapper<Product>().eq("flag", company.getFlag()));
        if (Cools.isEmpty(product)) {
            throw new CoolException(CodeRes.EMPTY);
        }
        Merchant merchant = merchantService.poll(product.getId());
        PayConfig payConfig = new PayConfig(merchant.getPrivateKey(), merchant.getAppId(), merchant.getPartner(), product.getNotifyUrl(), null, null, merchant.getSubject());
        // 支付日志
        PayRecord payRecord = new PayRecord(
            product.getId(),    // 所属接口[非空]
            merchant.getId(),    // 所属商户[非空]
            orderId,    // 外部订单号[非空]
            money,    // 金额[非空]
             (short) 1,    // 支付状态[非空]
            new Date()    // 添加时间[非空]
        );
        payRecordService.insert(payRecord);
        // 请求支付串
        TPaymentService service = PayUtils.getPaymentService(company);
        return service.getAuth(payConfig, orderId, money, productId, clientIp, openId);
    }

    /**
     * 异步通知
     * @param notifyData 通知数据
     * @return the bool
     */
    public boolean executePayMoneyNotify(Object notifyData, PayCompanyType company) {
        TPaymentService service = PayUtils.getPaymentService(company);
        return service.asyncNotify(notifyData);
    }

    /**
     * 系统业务处理
     * @param out_trade_no 外部订单号
     * @param transaction_id 第三方支付系统订单号
     * @param buyer_email 支付人id
     */
    public synchronized void executePaySuccess(String out_trade_no, String transaction_id, String buyer_email) {
        // 更新支付日志
        PayRecord payRecord = payRecordService.selectOne(new EntityWrapper<PayRecord>().eq("out_trade_no", out_trade_no));
        payRecord.setState((short) 3);
        payRecordService.updateById(payRecord);
    }

    /**
     * 外部订单号轨迹获取支付配置
     * @param out_trade_no 外部订单号
     * @return 支付配置类
     */
    public PayConfig getPayConfig(String out_trade_no) {
        PayRecord payRecord = payRecordService.selectOne(new EntityWrapper<PayRecord>().eq("out_trade_no", out_trade_no));
        Merchant merchant = merchantService.selectById(payRecord.getMerchantId());
        if (Cools.isEmpty(merchant)) {
            return null;
        }
        return new PayConfig(merchant.getPrivateKey(), merchant.getAppId(), merchant.getPartner(), null, null, null, merchant.getSubject());
    }

}
