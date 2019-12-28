package com.cool.mmc.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cool.mmc.system.entity.OperateLog;
import com.cool.mmc.system.service.OperateLogService;
import com.core.annotations.ManagerAuth;
import com.core.common.Cools;
import com.core.common.DateUtils;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class OperateLogController extends AbstractBaseController {

    @Autowired
    private OperateLogService operateLogService;

    @RequestMapping("/operateLog")
    public String index(){
        return "operateLog/operateLog";
    }

    @RequestMapping("/operateLog_detail")
    public String detail(){
        return "operateLog/operateLog_detail";
    }

    @RequestMapping(value = "/operateLog/{id}/auth")
    @ResponseBody
    @ManagerAuth
    public R get(@PathVariable("id") Long id) {
        return R.ok(operateLogService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/operateLog/list/auth")
    @ResponseBody
    @ManagerAuth
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<OperateLog> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(operateLogService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/operateLog/edit/auth")
    @ResponseBody
    @ManagerAuth
    public R edit(OperateLog operateLog) {
        if (Cools.isEmpty(operateLog)){
            return R.error();
        }
        if (null == operateLog.getId()){
            operateLogService.insert(operateLog);
        } else {
            operateLogService.updateById(operateLog);
        }
        return R.ok();
    }

    @RequestMapping(value = "/operateLog/add/auth")
    @ResponseBody
    @ManagerAuth
    public R add(OperateLog operateLog) {
        operateLogService.insert(operateLog);
        return R.ok();
    }

	@RequestMapping(value = "/operateLog/update/auth")
    @ResponseBody
    @ManagerAuth
    public R update(OperateLog operateLog){
        if (Cools.isEmpty(operateLog) || null==operateLog.getId()){
            return R.error();
        }
        operateLogService.updateById(operateLog);
        return R.ok();
    }

    @RequestMapping(value = "/operateLog/delete/auth")
    @ResponseBody
    @ManagerAuth
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        operateLogService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/operateLog/export/auth")
    @ResponseBody
    @ManagerAuth
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<OperateLog> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("operateLog"));
        convert(map, wrapper);
        List<OperateLog> list = operateLogService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/operateLogQuery/auth")
    @ResponseBody
    @ManagerAuth
    public R query(String condition) {
        EntityWrapper<OperateLog> wrapper = new EntityWrapper<>();
        wrapper.like("id", condition);
        Page<OperateLog> page = operateLogService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (OperateLog operateLog : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", operateLog.getId());
            map.put("value", operateLog.getId());
            result.add(map);
        }
        return R.ok(result);
    }

}
