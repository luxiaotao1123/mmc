package com.cool.mmc.api;

import com.cool.mmc.common.entity.WxPayData;
import com.cool.mmc.common.entity.enums.PayCompanyType;
import com.cool.mmc.common.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by vincent on 2019-12-30
 */
@Controller
@RequestMapping("/callback/v1")
public class CallBackController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(value = "/wechat/codeurl_notify", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String weChatCodeUrl(HttpServletRequest request) throws Exception {
        System.out.println("回调！");
        WxPayData res = new WxPayData();
        try {
            InputStream input = request.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int count;
            while ((count = input.read(data, 0, 1024)) != -1)
                outStream.write(data, 0, count);
            String result = new String(outStream.toByteArray(), StandardCharsets.UTF_8);
            System.out.println(result);
            if (true){
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            }else {
                res.setValue("return_code", "FAIL");
                res.setValue("return_msg", "FAIL");
            }
        } catch (Exception e) {
            res.setValue("return_code", "FAIL");
            res.setValue("return_msg", "支付结果中微信订单号不存在");
        }
        return res.toXml();
    }


    @RequestMapping(value ="/wechat/native_notify", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String nativeNotify(HttpServletRequest request) throws Exception {
        System.out.println("回调！");
        WxPayData res = new WxPayData();
        try {
            InputStream input = request.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int count;
            while ((count = input.read(data, 0, 1024)) != -1)
                outStream.write(data, 0, count);
            String result = new String(outStream.toByteArray(), StandardCharsets.UTF_8);
            if (paymentService.executePayMoneyNotify(result, PayCompanyType.wxNative)) {
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            }else {
                res.setValue("return_code", "FAIL");
                res.setValue("return_msg", "FAIL");
            }
        } catch (Exception e) {
            res.setValue("return_code", "FAIL");
            res.setValue("return_msg", "支付结果中微信订单号不存在");
        }
        return res.toXml();
    }


    @RequestMapping(value = "/wechat/h5_notify", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String h5Notify(HttpServletRequest request) throws Exception {
        System.out.println("回调！");
        WxPayData res = new WxPayData();
        try {
            InputStream input = request.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int count;
            while ((count = input.read(data, 0, 1024)) != -1)
                outStream.write(data, 0, count);
            String result = new String(outStream.toByteArray(), StandardCharsets.UTF_8);
            if (paymentService.executePayMoneyNotify(result, PayCompanyType.wxH5)) {
                return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            }else {
                res.setValue("return_code", "FAIL");
                res.setValue("return_msg", "FAIL");
            }
        } catch (Exception e) {
            res.setValue("return_code", "FAIL");
            res.setValue("return_msg", "支付结果中微信订单号不存在");
        }
        return res.toXml();
    }


}
