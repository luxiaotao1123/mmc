package com.cool.mmc.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cool.mmc.system.entity.Host;
import com.cool.mmc.system.service.HostService;
import com.core.common.Cools;
import com.core.common.DateUtils;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HostController extends AbstractBaseController {

    @Autowired
    private HostService hostService;

    @RequestMapping("/host")
    public String index(){
        return "host/host";
    }

    @RequestMapping("/host_detail")
    public String detail(){
        return "host/host_detail";
    }

    @RequestMapping(value = "/host/{id}/auth")
    @ResponseBody
    public R get(@PathVariable("id") Long id) {
        return R.ok(hostService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/host/list/auth")
    @ResponseBody
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<Host> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(hostService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/host/edit/auth")
    @ResponseBody
    public R edit(Host host) {
        if (Cools.isEmpty(host)){
            return R.error();
        }
        if (null == host.getId()){
            hostService.insert(host);
        } else {
            hostService.updateById(host);
        }
        return R.ok();
    }

    @RequestMapping(value = "/host/add/auth")
    @ResponseBody
    public R add(Host host) {
        hostService.insert(host);
        return R.ok();
    }

	@RequestMapping(value = "/host/update/auth")
    @ResponseBody
    public R update(Host host){
        if (Cools.isEmpty(host) || null==host.getId()){
            return R.error();
        }
        hostService.updateById(host);
        return R.ok();
    }

    @RequestMapping(value = "/host/delete/auth")
    @ResponseBody
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        hostService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/host/export/auth")
    @ResponseBody
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<Host> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("host"));
        convert(map, wrapper);
        List<Host> list = hostService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/hostQuery/auth")
    @ResponseBody
    public R query(String condition) {
        EntityWrapper<Host> wrapper = new EntityWrapper<>();
        wrapper.like("name", condition);
        Page<Host> page = hostService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Host host : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", host.getId());
            map.put("value", host.getName());
            result.add(map);
        }
        return R.ok(result);
    }

}
