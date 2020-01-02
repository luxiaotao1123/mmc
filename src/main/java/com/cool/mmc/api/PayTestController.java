package com.cool.mmc.api;

import com.cool.mmc.common.entity.enums.PayCompanyType;
import com.cool.mmc.common.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by vincent on 2019-12-30
 */
@RestController
@RequestMapping("/pay")
public class PayTestController {

    @Autowired
    private PaymentService paymentService;


    @GetMapping("/wx/h5")
    public String wxH5(@RequestParam String no){
        Object result = paymentService.executePayMoney(PayCompanyType.wxH5, no, 0.1, "47.96.118.52", null, "232");
        return String.valueOf(result);
    }

    @GetMapping("/wx/native")
    public String wxNative(@RequestParam String no){
        Object result = paymentService.executePayMoney(PayCompanyType.wxNative, no, 0.1, "47.96.118.52", null, "232");
        return String.valueOf(result);
    }


}
