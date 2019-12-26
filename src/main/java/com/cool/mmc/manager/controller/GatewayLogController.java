package com.cool.mmc.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.core.common.DateUtils;
import com.cool.mmc.manager.entity.GatewayLog;
import com.cool.mmc.manager.service.GatewayLogService;
import com.core.common.Cools;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class GatewayLogController extends AbstractBaseController {

    @Autowired
    private GatewayLogService gatewayLogService;

    @RequestMapping("/gatewayLog")
    public String index(){
        return "gatewayLog/gatewayLog";
    }

    @RequestMapping("/gatewayLog_detail")
    public String detail(){
        return "gatewayLog/gatewayLog_detail";
    }

    @RequestMapping(value = "/gatewayLog/{id}/auth")
    @ResponseBody
    public R get(@PathVariable("id") Long id) {
        return R.ok(gatewayLogService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/gatewayLog/list/auth")
    @ResponseBody
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<GatewayLog> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(gatewayLogService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/gatewayLog/edit/auth")
    @ResponseBody
    public R edit(GatewayLog gatewayLog) {
        if (Cools.isEmpty(gatewayLog)){
            return R.error();
        }
        if (null == gatewayLog.getId()){
            gatewayLogService.insert(gatewayLog);
        } else {
            gatewayLogService.updateById(gatewayLog);
        }
        return R.ok();
    }

    @RequestMapping(value = "/gatewayLog/add/auth")
    @ResponseBody
    public R add(GatewayLog gatewayLog) {
        gatewayLogService.insert(gatewayLog);
        return R.ok();
    }

	@RequestMapping(value = "/gatewayLog/update/auth")
    @ResponseBody
    public R update(GatewayLog gatewayLog){
        if (Cools.isEmpty(gatewayLog) || null==gatewayLog.getId()){
            return R.error();
        }
        gatewayLogService.updateById(gatewayLog);
        return R.ok();
    }

    @RequestMapping(value = "/gatewayLog/delete/auth")
    @ResponseBody
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        gatewayLogService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/gatewayLog/export/auth")
    @ResponseBody
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<GatewayLog> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("gatewayLog"));
        convert(map, wrapper);
        List<GatewayLog> list = gatewayLogService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/gatewayLogQuery/auth")
    @ResponseBody
    public R query(String condition) {
        EntityWrapper<GatewayLog> wrapper = new EntityWrapper<>();
        wrapper.like("id", condition);
        Page<GatewayLog> page = gatewayLogService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (GatewayLog gatewayLog : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", gatewayLog.getId());
            map.put("value", gatewayLog.getId());
            result.add(map);
        }
        return R.ok(result);
    }

}
