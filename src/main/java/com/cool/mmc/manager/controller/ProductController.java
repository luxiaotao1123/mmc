package com.cool.mmc.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.core.common.DateUtils;
import com.cool.mmc.manager.entity.Product;
import com.cool.mmc.manager.service.ProductService;
import com.core.annotations.ManagerAuth;
import com.core.common.Cools;
import com.core.common.R;
import com.core.controller.AbstractBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ProductController extends AbstractBaseController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/product")
    public String index(){
        return "product/product";
    }

    @RequestMapping("/product_detail")
    public String detail(){
        return "product/product_detail";
    }

    @RequestMapping(value = "/product/{id}/auth")
    @ResponseBody
    @ManagerAuth
    public R get(@PathVariable("id") Long id) {
        return R.ok(productService.selectById(String.valueOf(id)));
    }

    @RequestMapping(value = "/product/list/auth")
    @ResponseBody
    @ManagerAuth
    public R list(@RequestParam(defaultValue = "1")Integer curr,
                  @RequestParam(defaultValue = "10")Integer limit,
                  @RequestParam Map<String, Object> param){
        excludeTrash(param);
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        convert(param, wrapper);
        wrapper.orderBy("id", false);
        return R.ok(productService.selectPage(new Page<>(curr, limit), wrapper));
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

    @RequestMapping(value = "/product/edit/auth")
    @ResponseBody
    @ManagerAuth
    public R edit(Product product) {
        if (Cools.isEmpty(product)){
            return R.error();
        }
        if (null == product.getId()){
            productService.insert(product);
        } else {
            productService.updateById(product);
        }
        return R.ok();
    }

    @RequestMapping(value = "/product/add/auth")
    @ResponseBody
    @ManagerAuth
    public R add(Product product) {
        productService.insert(product);
        return R.ok();
    }

	@RequestMapping(value = "/product/update/auth")
    @ResponseBody
    public R update(Product product){
        if (Cools.isEmpty(product) || null==product.getId()){
            return R.error();
        }
        productService.updateById(product);
        return R.ok();
    }

    @RequestMapping(value = "/product/delete/auth")
    @ResponseBody
    @ManagerAuth
    public R delete(Integer[] ids){
        if (Cools.isEmpty(ids)){
            return R.error();
        }
        productService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping(value = "/product/export/auth")
    @ResponseBody
    @ManagerAuth
    public R export(@RequestBody JSONObject param){
        List<String> fields = JSONObject.parseArray(param.getJSONArray("fields").toJSONString(), String.class);
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        Map<String, Object> map = excludeTrash(param.getJSONObject("product"));
        convert(map, wrapper);
        List<Product> list = productService.selectList(wrapper);
        return R.ok(exportSupport(list, fields));
    }

    @RequestMapping(value = "/productQuery/auth")
    @ResponseBody
    @ManagerAuth
    public R query(String condition) {
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.like("name", condition);
        Page<Product> page = productService.selectPage(new Page<>(0, 10), wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Product product : page.getRecords()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("value", product.getName());
            result.add(map);
        }
        return R.ok(result);
    }

}
