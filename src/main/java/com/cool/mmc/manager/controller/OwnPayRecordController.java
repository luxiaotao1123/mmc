package com.cool.mmc.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cool.mmc.common.web.BaseController;
import com.cool.mmc.manager.entity.Merchant;
import com.cool.mmc.manager.entity.PayRecord;
import com.cool.mmc.manager.service.MerchantService;
import com.cool.mmc.manager.service.PayRecordService;
import com.core.annotations.ManagerAuth;
import com.core.common.Cools;
import com.core.common.DateUtils;
import com.core.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class OwnPayRecordController extends BaseController {

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private MerchantService merchantService;

    @RequestMapping("/ownPayRecord")
    public String index(){
        return "ownPayRecord/ownPayRecord";
    }

    @RequestMapping("/own_payRecord_detail")
    public String detail(){
        return "ownPayRecord/ownPayRecord_detail";
    }


    @RequestMapping(value = "/ownPayRecord/list/auth")
    @ResponseBody
    @ManagerAuth
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        List<Merchant> merchants = merchantService.selectList(new EntityWrapper<Merchant>().eq("user_id", getUserId()));
        Set<Long> set = new HashSet<>();
        if (merchants != null && merchants.size() > 0) {
            merchants.forEach(merchant -> {
                set.add(merchant.getId());
            });
        }
        if (set.isEmpty()) {
            set.add(-1L);
        }
        Wrapper<PayRecord> wrapper = new EntityWrapper<PayRecord>().in("merchant_id", set);
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(payRecordService.selectPage(new Page<>(curr, limit), wrapper));
    }

    private void convert(Map<String, Object> map, Wrapper wrapper){
        for (Map.Entry<String, Object> entry : map.entrySet()){
            if (entry.getKey().endsWith(">")) {
                wrapper.ge(Cools.deleteChar(entry.getKey()), DateUtils.convert(String.valueOf(entry.getValue())));
            } else if (entry.getKey().endsWith("<")) {
                wrapper.le(Cools.deleteChar(entry.getKey()), DateUtils.convert(String.valueOf(entry.getValue())));
            } else {
                wrapper.like(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
    }

    @RequestMapping(value = "/ownPayRecord/export/auth")
    @ResponseBody
    @ManagerAuth
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        List<Merchant> merchants = merchantService.selectList(new EntityWrapper<Merchant>().eq("user_id", getUserId()));
        Set<Long> set = new HashSet<>();
        if (merchants != null && merchants.size() > 0) {
            merchants.forEach(merchant -> {
                set.add(merchant.getId());
            });
        }
        if (set.isEmpty()) {
            set.add(-1L);
        }
        Wrapper<PayRecord> wrapper = new EntityWrapper<PayRecord>().in("merchant_id", set);
        Map<String, Object> map = excludeTrash(param.getJSONObject("payRecord"));
        convert(map, wrapper);
        List<PayRecord> list = payRecordService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }


}
