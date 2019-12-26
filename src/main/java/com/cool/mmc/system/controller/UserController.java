package com.cool.mmc.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cool.mmc.system.entity.User;
import com.cool.mmc.system.service.UserService;
import com.core.common.Cools;
import com.core.common.DateUtils;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class UserController extends AbstractBaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user")
    public String index(){
        return "user/user";
    }

    @RequestMapping("/user_detail")
    public String detail(){
        return "user/user_detail";
    }

    @RequestMapping(value = "/user/{id}/auth")
    @ResponseBody
    public R get(@PathVariable("id") Long id) {
        return R.ok(userService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/user/list/auth")
    @ResponseBody
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(userService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/user/edit/auth")
    @ResponseBody
    public R edit(User user) {
        if (Cools.isEmpty(user)){
            return R.error();
        }
        if (null == user.getId()){
            userService.insert(user);
        } else {
            userService.updateById(user);
        }
        return R.ok();
    }

    @RequestMapping(value = "/user/add/auth")
    @ResponseBody
    public R add(User user) {
        userService.insert(user);
        return R.ok();
    }

	@RequestMapping(value = "/user/update/auth")
    @ResponseBody
    public R update(User user){
        if (Cools.isEmpty(user) || null==user.getId()){
            return R.error();
        }
        User entity = userService.selectById(user.getId());
        entity.setUsername(user.getUsername());
        entity.setMobile(user.getMobile());
        userService.updateById(entity);
        return R.ok();
    }

    @RequestMapping(value = "/user/delete/auth")
    @ResponseBody
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        userService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/user/export/auth")
    @ResponseBody
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("user"));
        convert(map, wrapper);
        List<User> list = userService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/userQuery/auth")
    @ResponseBody
    public R query(String condition) {
        EntityWrapper<User> wrapper = new EntityWrapper<>();
        wrapper.like("username", condition);
        Page<User> page = userService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (User user : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("value", user.getUsername());
            result.add(map);
        }
        return R.ok(result);
    }

}
