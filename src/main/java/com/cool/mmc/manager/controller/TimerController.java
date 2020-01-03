package com.cool.mmc.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.core.common.DateUtils;
import com.cool.mmc.manager.entity.Timer;
import com.cool.mmc.manager.service.TimerService;
import com.core.common.Cools;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class TimerController extends AbstractBaseController {

    @Autowired
    private TimerService timerService;

    @RequestMapping("/timer")
    public String index(){
        return "timer/timer";
    }

    @RequestMapping("/timer_detail")
    public String detail(){
        return "timer/timer_detail";
    }

    @RequestMapping(value = "/timer/{id}/auth")
    @ResponseBody
    public R get(@PathVariable("id") Long id) {
        return R.ok(timerService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/timer/list/auth")
    @ResponseBody
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<Timer> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(timerService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/timer/edit/auth")
    @ResponseBody
    public R edit(Timer timer) {
        if (Cools.isEmpty(timer)){
            return R.error();
        }
        if (null == timer.getId()){
            timerService.insert(timer);
        } else {
            timerService.updateById(timer);
        }
        return R.ok();
    }

    @RequestMapping(value = "/timer/add/auth")
    @ResponseBody
    public R add(Timer timer) {
        timerService.insert(timer);
        return R.ok();
    }

	@RequestMapping(value = "/timer/update/auth")
    @ResponseBody
    public R update(Timer timer){
        if (Cools.isEmpty(timer) || null==timer.getId()){
            return R.error();
        }
        timerService.updateById(timer);
        return R.ok();
    }

    @RequestMapping(value = "/timer/delete/auth")
    @ResponseBody
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        timerService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/timer/export/auth")
    @ResponseBody
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<Timer> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("timer"));
        convert(map, wrapper);
        List<Timer> list = timerService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/timerQuery/auth")
    @ResponseBody
    public R query(String condition) {
        EntityWrapper<Timer> wrapper = new EntityWrapper<>();
        wrapper.like("id", condition);
        Page<Timer> page = timerService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Timer timer : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", timer.getId());
            map.put("value", timer.getId());
            result.add(map);
        }
        return R.ok(result);
    }

}
