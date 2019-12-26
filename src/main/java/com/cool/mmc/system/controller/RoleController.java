package com.cool.mmc.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cool.mmc.system.entity.Role;
import com.cool.mmc.system.service.RoleService;
import com.core.common.Cools;
import com.core.common.DateUtils;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class RoleController extends AbstractBaseController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/role")
    public String index(){
        return "role/role";
    }

    @RequestMapping("/role_detail")
    public String detail(){
        return "role/role_detail";
    }

    @RequestMapping("/role_power_detail")
    public String powerDetail(){
        return "role/role_power_detail";
    }

    @RequestMapping(value = "/role/{id}/auth")
    @ResponseBody
    public R get(@PathVariable("id") Long id) {
        return R.ok(roleService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/role/list/auth")
    @ResponseBody
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<Role> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(roleService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/role/edit/auth")
    @ResponseBody
    public R edit(Role role) {
        if (Cools.isEmpty(role)){
            return R.error();
        }
        if (null == role.getId()){
            roleService.insert(role);
        } else {
            roleService.updateById(role);
        }
        return R.ok();
    }

    @RequestMapping(value = "/role/add/auth")
    @ResponseBody
    public R add(Role role) {
        roleService.insert(role);
        return R.ok();
    }

	@RequestMapping(value = "/role/update/auth")
    @ResponseBody
    public R update(Role role){
        if (Cools.isEmpty(role) || null==role.getId()){
            return R.error();
        }
        roleService.updateById(role);
        return R.ok();
    }

    @RequestMapping(value = "/role/delete/auth")
    @ResponseBody
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        roleService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/role/export/auth")
    @ResponseBody
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<Role> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("role"));
        convert(map, wrapper);
        List<Role> list = roleService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/roleQuery/auth")
    @ResponseBody
    public R query(String condition) {
        EntityWrapper<Role> wrapper = new EntityWrapper<>();
        wrapper.like("name", condition);
        Page<Role> page = roleService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Role role : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", role.getId());
            map.put("value", role.getName());
            result.add(map);
        }
        return R.ok(result);
    }

}
