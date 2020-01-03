package com.cool.mmc.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.core.common.DateUtils;
import com.cool.mmc.manager.entity.Oauth;
import com.cool.mmc.manager.service.OauthService;
import com.core.common.Cools;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class OauthController extends AbstractBaseController {

    @Autowired
    private OauthService oauthService;

    @RequestMapping("/oauth")
    public String index(){
        return "oauth/oauth";
    }

    @RequestMapping("/oauth_detail")
    public String detail(){
        return "oauth/oauth_detail";
    }

    @RequestMapping(value = "/oauth/{id}/auth")
    @ResponseBody
    public R get(@PathVariable("id") Long id) {
        return R.ok(oauthService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/oauth/list/auth")
    @ResponseBody
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<Oauth> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(oauthService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/oauth/edit/auth")
    @ResponseBody
    public R edit(Oauth oauth) {
        if (Cools.isEmpty(oauth)){
            return R.error();
        }
        if (null == oauth.getId()){
            oauthService.insert(oauth);
        } else {
            oauthService.updateById(oauth);
        }
        return R.ok();
    }

    @RequestMapping(value = "/oauth/add/auth")
    @ResponseBody
    public R add(Oauth oauth) {
        oauthService.insert(oauth);
        return R.ok();
    }

	@RequestMapping(value = "/oauth/update/auth")
    @ResponseBody
    public R update(Oauth oauth){
        if (Cools.isEmpty(oauth) || null==oauth.getId()){
            return R.error();
        }
        oauthService.updateById(oauth);
        return R.ok();
    }

    @RequestMapping(value = "/oauth/delete/auth")
    @ResponseBody
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        oauthService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/oauth/export/auth")
    @ResponseBody
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<Oauth> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("oauth"));
        convert(map, wrapper);
        List<Oauth> list = oauthService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/oauthQuery/auth")
    @ResponseBody
    public R query(String condition) {
        EntityWrapper<Oauth> wrapper = new EntityWrapper<>();
        wrapper.like("id", condition);
        Page<Oauth> page = oauthService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Oauth oauth : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", oauth.getId());
            map.put("value", oauth.getId());
            result.add(map);
        }
        return R.ok(result);
    }

}
