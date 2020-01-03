package com.cool.mmc.common.pay.impl;

import com.cool.mmc.common.CodeRes;
import com.cool.mmc.common.entity.H5WxPayConfig;
import com.cool.mmc.common.entity.IWxPayConfig;
import com.cool.mmc.common.entity.WxPayData;
import com.cool.mmc.common.pay.WxPaymentServiceSupport;
import com.cool.mmc.common.service.PaymentService;
import com.cool.mmc.common.utils.HttpTools;
import com.cool.mmc.manager.entity.Merchant;
import com.core.common.Arith;
import com.core.common.Cools;
import com.core.exception.CoolException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

            System.out.println(result);
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
        String out_trade_no;
        String notifyStr = (String) notifyData;
        System.out.println(notifyStr);
        if (Cools.isEmpty(notifyStr)) {
            return false;
        }
        // 转换数据格式并验证签名
        WxPayData wxPayData = new WxPayData();

        try {
            IWxPayConfig wxPayConfig = new H5WxPayConfig();
            wxPayData.setValues(wxPayConfig);
            wxPayData.fromXml(notifyStr);
            out_trade_no = wxPayData.getValue("out_trade_no").toString();

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


    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "weixin://wap/pay?prepayid%3Dwx03094832961805ed1e075bb51304517000&package=1585930875&noncestr=1578016381&sign=56fc17dfbd61ef0d012a6097bf021ccb";
        String redirect_url = "";
        String result = s.concat("redirect_url").concat(URLEncoder.encode(redirect_url, "utf-8"));
        System.out.println(result);
    }

}
