package com.cool.mmc.api;

import com.cool.mmc.common.pay.TPaymentService;
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
    private TPaymentService wxH5Service;

    @Autowired
    private TPaymentService wxNativeService;

    @GetMapping("/wx/h5")
    public String wxH5(){

        Object result = wxH5Service.getAuth("dadsadsad11", 10.0,"999", "47.96.118.52", null);
        return String.valueOf(result);
    }

    @GetMapping("/wx/native")
    public String wxNative(@RequestParam String no){

        Object result = wxNativeService.getAuth(no, 0.1,"999", "47.96.118.52", null);
        return String.valueOf(result);
    }


}
