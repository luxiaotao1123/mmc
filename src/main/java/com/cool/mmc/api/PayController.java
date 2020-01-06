package com.cool.mmc.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.api.tools.IpTool;
import com.cool.mmc.api.tools.MD5Tool;
import com.cool.mmc.api.tools.Result;
import com.cool.mmc.common.entity.enums.PayCompanyType;
import com.cool.mmc.common.service.PaymentService;
import com.cool.mmc.manager.entity.Oauth;
import com.cool.mmc.manager.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/wx/h5")
    public Result wxH5(HttpServletRequest request, @RequestParam String appId, @RequestParam String orderId,
                       @RequestParam double money,@RequestParam String sign){
        Result result=new Result();
        Map<String,Object> map=new HashMap<>();
        Oauth check = check(appId,orderId,money, sign);
        if (check == null) {
            result.setCode("400");
            result.setMessage("签名验证失败");
            return result;
        }
        if(orderId == null ||money==0.00){
            result.setCode("400");
            result.setMessage("参数缺失");
            return result;
        }
        Object msg = paymentService.executePayMoney(PayCompanyType.wxH5, orderId, money, IpTool.getRemoteAddr(request),null, null,check.getId());
        if(msg == null){
            result.setCode("400");
            result.setMessage("订单创建失败");
            return result;
        }
        map.put("data",String.valueOf(msg));
        map.put("orderId",orderId);
        result.setData(map);
        result.setMessage("订单创建成功");
        result.setCode("200");
        return result;
    }

    @PostMapping("/wx/native")
    public Result wxNative(HttpServletRequest request, @RequestParam String appId, @RequestParam String orderId,
                           @RequestParam double money,@RequestParam String sign){
        Result result=new Result();
        Map<String,Object> map=new HashMap();
        Oauth check = check(appId,orderId,money, sign);
        if (check == null) {
            result.setCode("400");
            result.setMessage("签名验证失败");
            return result;
        }
        if(orderId == null ||money==0.00){
            result.setCode("400");
            result.setMessage("参数缺失");
            return result;
        }
        Object msg = paymentService.executePayMoney(PayCompanyType.wxNative, orderId, money, IpTool.getRemoteAddr(request),null, "232",check.getId());
        if(msg == null){
            result.setCode("400");
            result.setMessage("订单创建失败");
            return result;
        }
        map.put("data",String.valueOf(msg));
        map.put("orderId",orderId);
        result.setCode("200");
        result.setData(map);
        result.setMessage("订单创建成功");

        return result;
    }


    private Oauth check(String appId,String orderId,double money,String sign){
        Oauth oauth = oauthService.selectOne(new EntityWrapper<Oauth>().eq("account", appId));
        if(oauth==null){
            return null;
        }
        String str=appId+"&"+orderId+"&"+money;
        String encode = new MD5Tool(oauth.getSign(), "MD5").encode(str);
        if (encode.equals(sign)){
            return oauth;
        }
        return null;
    }


}

