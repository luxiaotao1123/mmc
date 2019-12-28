package com.cool.mmc.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.cool.mmc.system.entity.UserLogin;
import com.cool.mmc.system.service.UserLoginService;
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
public class UserLoginController extends AbstractBaseController {

    @Autowired
    private UserLoginService userLoginService;

    @RequestMapping("/userLogin")
    public String index(){
        return "userLogin/userLogin";
    }

    @RequestMapping("/userLogin_detail")
    public String detail(){
        return "userLogin/userLogin_detail";
    }

    @RequestMapping(value = "/userLogin/{id}/auth")
    @ResponseBody
    @ManagerAuth
    public R get(@PathVariable("id") Long id) {
        return R.ok(userLoginService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/userLogin/list/auth")
    @ResponseBody
    @ManagerAuth
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<UserLogin> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(userLoginService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/userLogin/edit/auth")
    @ResponseBody
    @ManagerAuth
    public R edit(UserLogin userLogin) {
        if (Cools.isEmpty(userLogin)){
            return R.error();
        }
        if (null == userLogin.getId()){
            userLoginService.insert(userLogin);
        } else {
            userLoginService.updateById(userLogin);
        }
        return R.ok();
    }

    @RequestMapping(value = "/userLogin/add/auth")
    @ResponseBody
    @ManagerAuth
    public R add(UserLogin userLogin) {
        userLoginService.insert(userLogin);
        return R.ok();
    }

	@RequestMapping(value = "/userLogin/update/auth")
    @ResponseBody
    @ManagerAuth
    public R update(UserLogin userLogin){
        if (Cools.isEmpty(userLogin) || null==userLogin.getId()){
            return R.error();
        }
        userLoginService.updateById(userLogin);
        return R.ok();
    }

    @RequestMapping(value = "/userLogin/delete/auth")
    @ResponseBody
    @ManagerAuth
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        userLoginService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/userLogin/export/auth")
    @ResponseBody
    @ManagerAuth
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<UserLogin> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("userLogin"));
        convert(map, wrapper);
        List<UserLogin> list = userLoginService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/userLoginQuery/auth")
    @ResponseBody
    @ManagerAuth
    public R query(String condition) {
        EntityWrapper<UserLogin> wrapper = new EntityWrapper<>();
        wrapper.like("token", condition);
        Page<UserLogin> page = userLoginService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserLogin userLogin : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", userLogin.getId());
            map.put("value", userLogin.getToken());
            result.add(map);
        }
        return R.ok(result);
    }

}
