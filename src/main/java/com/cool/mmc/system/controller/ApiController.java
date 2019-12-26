package com.cool.mmc.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cool.mmc.system.entity.Api;
import com.cool.mmc.system.service.ApiService;
import com.core.common.Cools;
import com.core.common.DateUtils;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ApiController extends AbstractBaseController {

    @Autowired
    private ApiService apiService;

    @RequestMapping("/api")
    public String index(){
        return "api/api";
    }

    @RequestMapping("/api_detail")
    public String detail(){
        return "api/api_detail";
    }

    @RequestMapping(value = "/api/{id}/auth")
    @ResponseBody
    public R get(@PathVariable("id") Long id) {
        return R.ok(apiService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/api/list/auth")
    @ResponseBody
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<Api> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(apiService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/api/edit/auth")
    @ResponseBody
    public R edit(Api api) {
        if (Cools.isEmpty(api)){
            return R.error();
        }
        if (null == api.getId()){
            apiService.insert(api);
        } else {
            apiService.updateById(api);
        }
        return R.ok();
    }

    @RequestMapping(value = "/api/add/auth")
    @ResponseBody
    public R add(Api api) {
        apiService.insert(api);
        return R.ok();
    }

	@RequestMapping(value = "/api/update/auth")
    @ResponseBody
    public R update(Api api){
        if (Cools.isEmpty(api) || null==api.getId()){
            return R.error();
        }
        apiService.updateById(api);
        return R.ok();
    }

    @RequestMapping(value = "/api/delete/auth")
    @ResponseBody
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        apiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/api/export/auth")
    @ResponseBody
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<Api> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("api"));
        convert(map, wrapper);
        List<Api> list = apiService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/apiQuery/auth")
    @ResponseBody
    public R query(String condition) {
        EntityWrapper<Api> wrapper = new EntityWrapper<>();
        wrapper.like("id", condition);
        Page<Api> page = apiService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Api api : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", api.getId());
            map.put("value", api.getId());
            result.add(map);
        }
        return R.ok(result);
    }

}
