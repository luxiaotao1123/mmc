package com.cool.mmc.api;

import com.cool.mmc.common.entity.WxPayData;
import com.cool.mmc.common.pay.TPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/wechat/codeurl_notify")
    @ResponseBody
    public String weChatCodeUrl(HttpServletRequest request) throws Exception {
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

    @Autowired
    private TPaymentService wxNativeService;

    @RequestMapping("/wechat/native_notify")
    @ResponseBody
    public String nativeNotify(HttpServletRequest request) throws Exception {
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
            if (wxNativeService.asyncNotify(result)){
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
