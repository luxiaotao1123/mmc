//package com.cool.mmc.common.pay.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.AlipayClient;
//import com.alipay.api.DefaultAlipayClient;
//import com.alipay.api.domain.AlipayTradeAppPayModel;
//import com.alipay.api.request.AlipayTradeAppPayRequest;
//import com.alipay.api.response.AlipayTradeAppPayResponse;
//import com.cool.mmc.common.CodeRes;
//import com.cool.mmc.common.pay.TPaymentService;
//import com.cool.mmc.manager.entity.Merchant;
//import com.core.common.Cools;
//import com.core.exception.CoolException;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
///**
// * 支付宝APP支付服务类
// * Created by vincent on 2019-12-26
// */
//@Service
//public class AlipayService implements TPaymentService {
//
//    private static final String RSA_ALIPAY_URL = "";
//
//    private static final String NOTIFY_URL = "";
//
//
//    @Override
//    public Object getAuth(String outTradeNo, Double money, String clientIp, String openId) {
//        if (Cools.isEmpty(outTradeNo) || Cools.isEmpty(money)){
//            throw new CoolException(CodeRes.COMMON_00001);
//        }
//        Merchant merchant = new Merchant();
//        //实例化客户端
//        AlipayClient alipayClient = new DefaultAlipayClient(
//                RSA_ALIPAY_URL,
//                merchant.getAppId(),
//                merchant.getPrivateKey(),
//                "json",
//                "UTF-8",
//                merchant.getPublicKey(),
//                "RSA2");
//        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
//        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
//        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//        model.setBody(merchant.getSubject());
//        model.setSubject(merchant.getSubject());
//        model.setOutTradeNo(outTradeNo);
//        model.setTimeoutExpress("30m");
//        model.setTotalAmount(String.valueOf(money));
//        model.setProductCode("QUICK_MSECURITY_PAY");
//        request.setBizModel(model);
//        request.setNotifyUrl(NOTIFY_URL);
//        try {
//            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("body", response.getBody());
//            jsonObject.put("tradeNo", response.getTradeNo());
//            return jsonObject.toJSONString();
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//            throw new CoolException(CodeRes.PAY_20001);
//        }
//    }
//
//    @Override
//    public boolean asyncNotify(Object notifyData) {
//        @SuppressWarnings("unchecked")
//        Map<String, String> params = (Map<String, String>) notifyData;
//        // 商户订单号
//        String out_trade_no = params.get("out_trade_no");
//        // 支付宝交易号
//        String trade_no = params.get("trade_no") != null ? params.get("trade_no") : "";
//        // 交易状态
//        String trade_status = "";
//        if (params.get("trade_status") != null) {
//            trade_status = params.get("trade_status");
//        } else if (params.get("success") != null) {
//            trade_status = params.get("success");
//        }
//        // 支付人支付宝帐号
//        String buyer_email = params.get("buyer_email") != null ? buyer_email = params.get("buyer_email") : "";
//
//        // 校验返回值
//        boolean checkV1;
//        try {
//            checkV1 = false;
////            checkV1 = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "utf-8", "RSA2");
//        }catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        if (!checkV1){
//            return false;
//        }
//
//        switch (trade_status) {
//            case "TRADE_FINISHED":
//                break;
//            case "TRADE_SUCCESS":
//                // todo
////                PaymentService.getBean().executePaySuccess(out_trade_no, trade_no, buyer_email);
//                break;
//            case "true":
//                // todo
////                PaymentService.getBean().executePaySuccess(out_trade_no, trade_no, buyer_email);
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
//
//}
