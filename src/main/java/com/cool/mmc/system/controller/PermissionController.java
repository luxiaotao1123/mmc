package com.cool.mmc.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cool.mmc.system.entity.Permission;
import com.cool.mmc.system.service.PermissionService;
import com.core.common.Cools;
import com.core.common.DateUtils;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class PermissionController extends AbstractBaseController {

    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/permission")
    public String index(){
        return "permission/permission";
    }

    @RequestMapping("/permission_detail")
    public String detail(){
        return "permission/permission_detail";
    }

    @RequestMapping(value = "/permission/{id}/auth")
    @ResponseBody
    public R get(@PathVariable("id") Long id) {
        return R.ok(permissionService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/permission/list/auth")
    @ResponseBody
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<Permission> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(permissionService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/permission/edit/auth")
    @ResponseBody
    public R edit(Permission permission) {
        if (Cools.isEmpty(permission)){
            return R.error();
        }
        if (null == permission.getId()){
            permissionService.insert(permission);
        } else {
            permissionService.updateById(permission);
        }
        return R.ok();
    }

    @RequestMapping(value = "/permission/add/auth")
    @ResponseBody
    public R add(Permission permission) {
        permissionService.insert(permission);
        return R.ok();
    }

	@RequestMapping(value = "/permission/update/auth")
    @ResponseBody
    public R update(Permission permission){
        if (Cools.isEmpty(permission) || null==permission.getId()){
            return R.error();
        }
        permissionService.updateById(permission);
        return R.ok();
    }

    @RequestMapping(value = "/permission/delete/auth")
    @ResponseBody
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        permissionService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/permission/export/auth")
    @ResponseBody
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<Permission> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("permission"));
        convert(map, wrapper);
        List<Permission> list = permissionService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/permissionQuery/auth")
    @ResponseBody
    public R query(String condition) {
        EntityWrapper<Permission> wrapper = new EntityWrapper<>();
        wrapper.like("name", condition);
        Page<Permission> page = permissionService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Permission permission : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", permission.getId());
            map.put("value", permission.getName());
            result.add(map);
        }
        return R.ok(result);
    }

}
