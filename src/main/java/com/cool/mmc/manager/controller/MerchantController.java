package com.cool.mmc.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.core.common.DateUtils;
import com.cool.mmc.manager.entity.Merchant;
import com.cool.mmc.manager.service.MerchantService;
import com.core.annotations.ManagerAuth;
import com.core.common.Cools;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MerchantController extends AbstractBaseController {

    @Autowired
    private MerchantService merchantService;

    @RequestMapping("/merchant")
    public String index(){
        return "merchant/merchant";
    }

    @RequestMapping("/merchant_detail")
    public String detail(){
        return "merchant/merchant_detail";
    }

    @RequestMapping(value = "/merchant/{id}/auth")
    @ResponseBody
    @ManagerAuth
    public R get(@PathVariable("id") Long id) {
        return R.ok(merchantService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/merchant/list/auth")
    @ResponseBody
    @ManagerAuth
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(merchantService.selectPage(new Page<>(curr, limit), wrapper));
    }

    private void convert(Map<String, Object> map, EntityWrapper wrapper){
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

    @RequestMapping(value = "/merchant/edit/auth")
    @ResponseBody
    @ManagerAuth
    public R edit(Merchant merchant) {
        if (Cools.isEmpty(merchant)){
            return R.error();
        }
        if (null == merchant.getId()){
            merchantService.insert(merchant);
        } else {
            merchantService.updateById(merchant);
        }
        return R.ok();
    }

    @RequestMapping(value = "/merchant/add/auth")
    @ResponseBody
    @ManagerAuth
    public R add(Merchant merchant) {
        merchantService.insert(merchant);
        return R.ok();
    }

	@RequestMapping(value = "/merchant/update/auth")
    @ResponseBody
    public R update(Merchant merchant){
        if (Cools.isEmpty(merchant) || null==merchant.getId()){
            return R.error();
        }
        merchantService.updateById(merchant);
        return R.ok();
    }

    @RequestMapping(value = "/merchant/delete/auth")
    @ResponseBody
    @ManagerAuth
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        merchantService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/merchant/export/auth")
    @ResponseBody
    @ManagerAuth
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("merchant"));
        convert(map, wrapper);
        List<Merchant> list = merchantService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/merchantQuery/auth")
    @ResponseBody
    @ManagerAuth
    public R query(String condition) {
        EntityWrapper<Merchant> wrapper = new EntityWrapper<>();
        wrapper.like("name", condition);
        Page<Merchant> page = merchantService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Merchant merchant : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", merchant.getId());
            map.put("value", merchant.getName());
            result.add(map);
        }
        return R.ok(result);
    }

}
