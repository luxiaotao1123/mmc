package com.cool.mmc.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cool.mmc.common.web.BaseController;
import com.cool.mmc.manager.entity.Merchant;
import com.cool.mmc.manager.service.MerchantService;
import com.core.annotations.ManagerAuth;
import com.core.common.Cools;
import com.core.common.DateUtils;
import com.core.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class OwnMerchantController extends BaseController {

    @Autowired
    private MerchantService merchantService;

    @RequestMapping("/ownMerchant")
    public String index(){
        return "ownmerchant/own_merchant";
    }

    @RequestMapping("/own_merchant_detail")
    public String detail(){
        return "ownmerchant/own_merchant_detail";
    }

    @RequestMapping(value = "/own_merchant/list/auth")
    @ResponseBody
    @ManagerAuth
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        Wrapper<Merchant> wrapper = new EntityWrapper<Merchant>().eq("user_id", getUserId());
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(merchantService.selectPage(new Page<>(curr, limit), wrapper));
    }

    private void convert(Map<String, Object> map, Wrapper<Merchant> wrapper){
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


    @RequestMapping(value = "/own_merchant/export/auth")
    @ResponseBody
    @ManagerAuth
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        Wrapper<Merchant> wrapper = new EntityWrapper<Merchant>().eq("user_id", getUserId());
        Map<String, Object> map = excludeTrash(param.getJSONObject("merchant"));
        convert(map, wrapper);
        List<Merchant> list = merchantService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

}
