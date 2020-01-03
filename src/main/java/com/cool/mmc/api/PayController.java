package com.cool.mmc.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.api.tools.IpTool;
import com.cool.mmc.api.tools.Result;
import com.cool.mmc.common.entity.enums.PayCompanyType;
import com.cool.mmc.common.service.PaymentService;
import com.cool.mmc.manager.entity.Oauth;
import com.cool.mmc.manager.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OauthService oauthService;

    @GetMapping("/wx/h5")
    public Result wxH5(HttpServletRequest request, @RequestParam String username, @RequestParam String key, @RequestParam String orderId,
                       @RequestParam double money){
        Result result=new Result();
        Map<String,Object> map=new HashMap<>();
        Oauth check = check(username, key);
        if (check == null) {
            result.setMessage("签名验证失败");
            return result;
        }
        if(orderId == null ||money==0.00){
            result.setMessage("参数缺失");
            return result;
        }
        Object msg = paymentService.executePayMoney(PayCompanyType.wxH5, orderId, money, IpTool.getRemoteAddr(request),null, null);
        if(msg == null){
            result.setMessage("订单创建失败");
            return result;
        }
        map.put("data",String.valueOf(msg));
        map.put("orderId",orderId);
        result.setData(map);
        result.setMessage("订单创建成功");

        return result;
    }

    @GetMapping("/wx/native")
    public Result wxNative(HttpServletRequest request, @RequestParam String username, @RequestParam String key, @RequestParam String orderId,
                           @RequestParam double money){
        Result result=new Result();
        Map<String,Object> map=new HashMap();
        Oauth check = check(username, key);
        if (check == null) {
            result.setMessage("签名验证失败");
            return result;
        }
        if(orderId == null ||money==0.00){
            result.setMessage("参数缺失");
            return result;
        }
        Object msg = paymentService.executePayMoney(PayCompanyType.wxNative, orderId, money, IpTool.getRemoteAddr(request),null, null);
        if(msg == null){
            result.setMessage("订单创建失败");
            return result;
        }
        map.put("data",String.valueOf(msg));
        map.put("orderId",orderId);
        result.setData(map);
        result.setMessage("订单创建成功");

        return result;
    }


    private Oauth check(String username,String key){
        Oauth oauth=new Oauth();
        oauth.setAccount(username);
        oauth.setSign(key);
        return oauthService.selectOne(new EntityWrapper<>(oauth));
    }


}

