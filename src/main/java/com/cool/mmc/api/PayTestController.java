package com.cool.mmc.api;

import com.cool.mmc.common.CodeRes;
import com.cool.mmc.common.entity.enums.PayCompanyType;
import com.cool.mmc.common.service.PaymentService;
import com.core.exception.CoolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vincent on 2019-12-30
 */
@RestController
@RequestMapping("/paytest")
public class PayTestController {

    @Autowired
    private PaymentService paymentService;


    @GetMapping("/wx/h5")
    public String wxH5(@RequestParam(required = false) String no){
        if (no == null) {
            throw new CoolException(CodeRes.COMMON_00001);
        }

        Object result = paymentService.executePayMoney(PayCompanyType.wxH5, no, 0.1, "47.96.118.52", null, "232", 1L);
        return String.valueOf(result);
    }

    @GetMapping("/wx/native")
    public String wxNative(@RequestParam String no){
        Object result = paymentService.executePayMoney(PayCompanyType.wxNative, no, 0.1, "47.96.118.52", null, "232", 1L);
        return String.valueOf(result);
    }

    @PostMapping("/testBack")
    public Map<String, Object> testBack(@RequestParam String orderId, @RequestParam String code){
        System.out.println(orderId);
        System.out.println(code);
        Map<String,Object> map=new HashMap<>();
        map.put("code","200");
        return map;
    }


}
