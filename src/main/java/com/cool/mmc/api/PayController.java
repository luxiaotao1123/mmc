package com.cool.mmc.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.api.tools.IpTool;
import com.cool.mmc.api.tools.Result;
import com.cool.mmc.common.entity.enums.PayCompanyType;
import com.cool.mmc.common.service.PaymentService;
import com.cool.mmc.common.utils.SignUtils;
import com.cool.mmc.manager.entity.Oauth;
import com.cool.mmc.manager.service.OauthService;
import com.core.common.Cools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vincent on 2019-12-30
 */
@RestController
@RequestMapping("/pay/v1")
public class PayController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OauthService oauthService;

    @PostMapping("/wx/h5")
    public Result wxH5(HttpServletRequest request
            , @RequestParam(value = "appId", required = false) String appId
            , @RequestParam(value = "outTradeNo", required = false) String outTradeNo
            , @RequestParam(value = "timestamp", required = false) String timestamp
            , @RequestParam(value = "money", required = false) Double money
            , @RequestParam(value = "sign", required = false) String sign) {

        // 非空校验
        if (Cools.isEmpty(appId) || Cools.isEmpty(outTradeNo) || Cools.isEmpty(timestamp) || Cools.isEmpty(money) || Cools.isEmpty(sign)) {
            return new Result(400, "参数错误");
        }
        if (money <= 0.0D) {
            return new Result(401, "金额错误");
        }
        // 账户校验
        Oauth oauth = oauthService.selectOne(new EntityWrapper<Oauth>().eq("account", appId));
        if (Cools.isEmpty(oauth) || oauth.getStatus() == 0) {
            return new Result(403, "账户已被禁用");
        }
        // 验签
        Map<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("outTradeNo", outTradeNo);
        param.put("timestamp", timestamp);
        param.put("money", money);
        if (!sign.equals(SignUtils.sign(param, oauth.getSign()))) {
            return new Result(402, "签名错误");
        }
        // 请求支付
        Object msg = paymentService.executePayMoney(PayCompanyType.wxH5, outTradeNo, money, IpTool.getRemoteAddr(request),null, "232",oauth.getId());
        // 返回结果
        Map<String,Object> map = new HashMap<>();
        map.put("mwebUrl", msg);
        return new Result(200, "请求支付成功", map);
    }

    @PostMapping("/wx/native")
    public Result wxNative(HttpServletRequest request
            , @RequestParam(value = "appId", required = false) String appId
            , @RequestParam(value = "outTradeNo", required = false) String outTradeNo
            , @RequestParam(value = "timestamp", required = false) String timestamp
            , @RequestParam(value = "money", required = false) Double money
            , @RequestParam(value = "sign", required = false) String sign) {

        // 非空校验
        if (Cools.isEmpty(appId) || Cools.isEmpty(outTradeNo) || Cools.isEmpty(timestamp) || Cools.isEmpty(money) || Cools.isEmpty(sign)) {
            return new Result(400, "参数错误");
        }
        if (money <= 0.0D) {
            return new Result(401, "金额错误");
        }
        // 账户校验
        Oauth oauth = oauthService.selectOne(new EntityWrapper<Oauth>().eq("account", appId));
        if (Cools.isEmpty(oauth) || oauth.getStatus() == 0) {
            return new Result(403, "账户已被禁用");
        }
        // 验签
        Map<String, Object> param = new HashMap<>();
        param.put("appId", appId);
        param.put("outTradeNo", outTradeNo);
        param.put("timestamp", timestamp);
        param.put("money", money);
        if (!sign.equals(SignUtils.sign(param, oauth.getSign()))) {
            return new Result(402, "签名错误");
        }
        // 请求支付
        Object msg = paymentService.executePayMoney(PayCompanyType.wxNative, outTradeNo, money, IpTool.getRemoteAddr(request),null, "232",oauth.getId());
        // 返回结果
        Map<String,Object> map = new HashMap<>();
        map.put("codeUrl", msg);
        return new Result(200, "请求支付成功", map);
    }

}

