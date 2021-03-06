package com.cool.mmc.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.core.common.DateUtils;
import com.cool.mmc.manager.entity.PayRecord;
import com.cool.mmc.manager.service.PayRecordService;
import com.core.annotations.ManagerAuth;
import com.core.common.Cools;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class PayRecordController extends AbstractBaseController {

    @Autowired
    private PayRecordService payRecordService;

    @RequestMapping("/payRecord")
    public String index(){
        return "payRecord/payRecord";
    }

    @RequestMapping("/payRecord_detail")
    public String detail(){
        return "payRecord/payRecord_detail";
    }

    @RequestMapping(value = "/payRecord/{id}/auth")
    @ResponseBody
    @ManagerAuth
    public R get(@PathVariable("id") Long id) {
        return R.ok(payRecordService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/payRecord/list/auth")
    @ResponseBody
    @ManagerAuth
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<PayRecord> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(payRecordService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/payRecord/edit/auth")
    @ResponseBody
    @ManagerAuth
    public R edit(PayRecord payRecord) {
        if (Cools.isEmpty(payRecord)){
            return R.error();
        }
        if (null == payRecord.getId()){
            payRecordService.insert(payRecord);
        } else {
            payRecordService.updateById(payRecord);
        }
        return R.ok();
    }

    @RequestMapping(value = "/payRecord/add/auth")
    @ResponseBody
    @ManagerAuth
    public R add(PayRecord payRecord) {
        payRecordService.insert(payRecord);
        return R.ok();
    }

	@RequestMapping(value = "/payRecord/update/auth")
    @ResponseBody
    public R update(PayRecord payRecord){
        if (Cools.isEmpty(payRecord) || null==payRecord.getId()){
            return R.error();
        }
        payRecordService.updateById(payRecord);
        return R.ok();
    }

    @RequestMapping(value = "/payRecord/delete/auth")
    @ResponseBody
    @ManagerAuth
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        payRecordService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/payRecord/export/auth")
    @ResponseBody
    @ManagerAuth
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<PayRecord> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("payRecord"));
        convert(map, wrapper);
        List<PayRecord> list = payRecordService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/payRecordQuery/auth")
    @ResponseBody
    @ManagerAuth
    public R query(String condition) {
        EntityWrapper<PayRecord> wrapper = new EntityWrapper<>();
        wrapper.like("id", condition);
        Page<PayRecord> page = payRecordService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (PayRecord payRecord : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", payRecord.getId());
            map.put("value", payRecord.getId());
            result.add(map);
        }
        return R.ok(result);
    }

}
