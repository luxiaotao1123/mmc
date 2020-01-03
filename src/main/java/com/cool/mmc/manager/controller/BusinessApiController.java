package com.cool.mmc.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.core.common.DateUtils;
import com.cool.mmc.manager.entity.BusinessApi;
import com.cool.mmc.manager.service.BusinessApiService;
import com.core.common.Cools;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class BusinessApiController extends AbstractBaseController {

    @Autowired
    private BusinessApiService businessApiService;

    @RequestMapping("/businessApi")
    public String index(){
        return "businessApi/businessApi";
    }

    @RequestMapping("/businessApi_detail")
    public String detail(){
        return "businessApi/businessApi_detail";
    }

    @RequestMapping(value = "/businessApi/{id}/auth")
    @ResponseBody
    public R get(@PathVariable("id") Long id) {
        return R.ok(businessApiService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/businessApi/list/auth")
    @ResponseBody
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<BusinessApi> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(businessApiService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/businessApi/edit/auth")
    @ResponseBody
    public R edit(BusinessApi businessApi) {
        if (Cools.isEmpty(businessApi)){
            return R.error();
        }
        if (null == businessApi.getId()){
            businessApiService.insert(businessApi);
        } else {
            businessApiService.updateById(businessApi);
        }
        return R.ok();
    }

    @RequestMapping(value = "/businessApi/add/auth")
    @ResponseBody
    public R add(BusinessApi businessApi) {
        businessApiService.insert(businessApi);
        return R.ok();
    }

	@RequestMapping(value = "/businessApi/update/auth")
    @ResponseBody
    public R update(BusinessApi businessApi){
        if (Cools.isEmpty(businessApi) || null==businessApi.getId()){
            return R.error();
        }
        businessApiService.updateById(businessApi);
        return R.ok();
    }

    @RequestMapping(value = "/businessApi/delete/auth")
    @ResponseBody
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        businessApiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/businessApi/export/auth")
    @ResponseBody
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<BusinessApi> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("businessApi"));
        convert(map, wrapper);
        List<BusinessApi> list = businessApiService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/businessApiQuery/auth")
    @ResponseBody
    public R query(String condition) {
        EntityWrapper<BusinessApi> wrapper = new EntityWrapper<>();
        wrapper.like("id", condition);
        Page<BusinessApi> page = businessApiService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (BusinessApi businessApi : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", businessApi.getId());
            map.put("value", businessApi.getId());
            result.add(map);
        }
        return R.ok(result);
    }

}
