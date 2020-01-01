//package com.cool.mmc.common.service;
//
//import org.springframework.stereotype.Service;
//
///**
// * Created by vincent on 2019-12-26
// */
//@Service
//public class PaymentService {
//
//    public Object executePayMoney(Long memberId, Long orderId, Double money, String clientIp, PayTypeEnum payType, PayCompanyType company, String openId, Boolean weChatV2) {
//        TPaymentService tPaymentService = PayUtils.getPaymentService(company);
//        String trackNo = tPaymentService.getTrackNo(orderId != null ? orderId : memberId, payType);
//        Object result = tPaymentService.getAuth(trackNo, money,clientIp, openId, weChatV2);
//        if (company.equals(PayCompanyType.weChat)){
//            trackNo = (null != weChatV2 && weChatV2)?trackNo+"N":trackNo+"O";
//        }
//        executeSavePayRecord(trackNo, memberId, money, payType, company);
//        return result;
//    }
//
//}
