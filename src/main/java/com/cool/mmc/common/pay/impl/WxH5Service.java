package com.cool.mmc.common.pay.impl;

import com.cool.mmc.common.entity.IWxPayConfig;
import com.cool.mmc.common.entity.PayConfig;
import com.cool.mmc.common.entity.WxPayData;
import com.cool.mmc.common.pay.WxPaymentServiceSupport;
import com.cool.mmc.common.service.PaymentService;
import com.cool.mmc.common.utils.HttpTools;
import com.core.common.Arith;
import com.core.common.Cools;
import com.core.exception.CoolException;
import org.springframework.stereotype.Service;

/**
 * 微信h5支付服务类
 * Created by vincent on 2019-12-28
 */
@Service("wxH5Service")
public class WxH5Service extends WxPaymentServiceSupport {


    @Override
    public Object getAuth(IWxPayConfig payConfig, String outTradeNo, Double money, String productId, String clientIp, String openId) {
        try {
            if(null == clientIp || !HttpTools.isboolIp(clientIp)){
                clientIp = "127.0.0.1";
            }
            int totalFee = (int) Arith.multiplys(0, money, 100);
            String result = getH5UnifiedOrderResult(
                    totalFee
                    , payConfig.getSubject()
                    , clientIp
                    , outTradeNo
                    , payConfig);
            WxPayData res = new WxPayData();
            res.setValues(payConfig);
            res.fromXml(result);
            if (res.getValue("return_code")!=null && !String.valueOf(res.getValue("return_code")).toUpperCase().equals("SUCCESS")) {
                throw new Exception(String.valueOf(res.getValue("return_msg")));
            }
            if (!res.checkSign()) {
                throw new Exception("签名验证失败"+result);
            }
            if (res.getValue("err_code")!=null) {
                throw new Exception(String.valueOf(res.getValue("err_code_des")));
            }

            return res.getValue("mweb_url");
        } catch (Exception e) {
            e.printStackTrace();
            throw new CoolException(e.getMessage());
        }
    }

    @Override
    public boolean asyncNotify(Object notifyData) {
        String out_trade_no;
        String notifyStr = (String) notifyData;
        System.out.println(notifyStr);
        if (Cools.isEmpty(notifyStr)) {
            return false;
        }
        // 转换数据格式并验证签名
        WxPayData wxPayData = new WxPayData();

        try {
            wxPayData.fromXml(notifyStr);
            out_trade_no = wxPayData.getValue("out_trade_no").toString();
            PayConfig payConfig = PaymentService.getBean().getPayConfig(out_trade_no);
            if (null == payConfig) {
                throw new Exception("payConfig验签失败，没有轨迹订单：[out_trade_no] ===>> "+out_trade_no);
            }
            wxPayData.setValues(payConfig);
            if (!wxPayData.checkSign()) {
                throw new Exception("签名验证失败"+notifyStr);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        // 检查支付结果中transaction_id是否存在
        if (!wxPayData.isSet("transaction_id")) {
            System.err.println("订单号不存在");
            return false;
        }

        String transaction_id = wxPayData.getValue("transaction_id").toString();

        // 在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱
        try {
            PaymentService.getBean().executePaySuccess(out_trade_no, transaction_id, String.valueOf(wxPayData.getValue("openid")));
            System.out.println("====>> " +transaction_id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
