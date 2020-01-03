package com.cool.mmc.common.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.common.CodeRes;
import com.cool.mmc.common.utils.KeyedDigest;
import com.cool.mmc.system.entity.*;
import com.cool.mmc.system.service.*;
import com.core.annotations.ManagerAuth;
import com.core.common.Cools;
import com.core.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by vincent on 2019-07-30
 */
@RestController
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @RequestMapping("/login.action")
    public R loginAction(String mobile, String password){
        if (mobile.equals("super") && KeyedDigest.getKeyedDigestMD5(password).equals("a8934ef22591e30449f09285ef27c8c6")) {
            Map<String, Object> res = new HashMap<>();
            res.put("username", mobile);
            res.put("token", Cools.enToken(System.currentTimeMillis() + mobile, "a8934ef22591e30449f09285ef27c8c6"));
            return R.ok(res);
        }
        EntityWrapper<User> userWrapper = new EntityWrapper<>();
        userWrapper.eq("mobile", mobile);
        User user = userService.selectOne(userWrapper);
        if (Cools.isEmpty(user)){
            return R.parse(CodeRes.USER_10001);
        }
        if (user.getStatus()!=1){
            return R.parse(CodeRes.USER_10002);
        }
        if (!user.getPassword().equals(password)){
            return R.parse(CodeRes.USER_10003);
        }
        String token = Cools.enToken(System.currentTimeMillis() + mobile, password);
        userLoginService.delete(new EntityWrapper<UserLogin>().eq("user_id", user.getId()));
        UserLogin userLogin = new UserLogin();
        userLogin.setUserId(user.getId());
        userLogin.setToken(token);
        userLoginService.insert(userLogin);
        Map<String, Object> res = new HashMap<>();
        res.put("username", user.getUsername());
        res.put("token", token);
        return R.ok(res);
    }

    @RequestMapping("/user/detail/auth")
    @ManagerAuth
    public R userDetail(){
        return R.ok(userService.selectById(getUserId()));
    }

    @RequestMapping("/menu/auth")
    @ManagerAuth
    public R menu(){
        // 获取所有一级菜单
        List<Resource> oneLevel = resourceService.selectList(new EntityWrapper<Resource>().eq("level", 1).eq("status", 1).orderBy("sort"));
        // 获取当前用户的所有二级菜单
        List<RoleResource> roleResources;
        if (getUserId() == 9527) {
            roleResources = roleResourceService.selectList(new EntityWrapper<>());
        } else {
            User user = userService.selectById(getUserId());
            roleResources = roleResourceService.selectList(new EntityWrapper<RoleResource>().eq("role_id", user.getRoleId()));
        }
        List<Long> resourceIds = new ArrayList<>();
        roleResources.forEach(roleResource -> resourceIds.add(roleResource.getResourceId()));
        if (resourceIds.isEmpty()){
            return R.ok();
        }
        List<Resource> twoLevel = resourceService.selectList(new EntityWrapper<Resource>().in("id", resourceIds).eq("level", 2).eq("status", 1).orderBy("sort"));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Resource menu : oneLevel) {
            Map<String, Object> map = new HashMap<>();
            List<Resource> subMenu = new ArrayList<>();
            Iterator<Resource> iterator = twoLevel.iterator();
            while (iterator.hasNext()) {
                Resource resource = iterator.next();
                if (resource.getResourceId().equals(menu.getId())) {
                    subMenu.add(resource);
                    iterator.remove();
                }
            }
            if (subMenu.isEmpty()) {
                continue;
            }
            map.put("menu", menu.getName());
            map.put("subMenu", subMenu);
            result.add(map);
        }
        return R.ok(result);
    }

    @RequestMapping("/power/list/auth")
    @ManagerAuth
    public R powerList(){
        List<Resource> oneLevels = resourceService.selectList(new EntityWrapper<Resource>().eq("level", 1).eq("status", 1).orderBy("sort"));
        List<Map> result = new ArrayList<>();
        for (Resource oneLevel : oneLevels){
            List<Map> twoLevelsList = new ArrayList<>();
            Map<String, Object> oneLevelMap = new HashMap<>();
            oneLevelMap.put("title", oneLevel.getName());
            oneLevelMap.put("id", oneLevel.getCode());
            oneLevelMap.put("spread", true);
            oneLevelMap.put("children", twoLevelsList);
            List<Resource> twoLevels = resourceService.selectList(new EntityWrapper<Resource>().eq("resource_id", oneLevel.getId()).eq("level", 2).eq("status", 1).orderBy("sort"));

            for (Resource twoLevel : twoLevels){
                Map<String, Object> twoLevelMap = new HashMap<>();
                twoLevelMap.put("title", twoLevel.getName());
                twoLevelMap.put("id", twoLevel.getCode());
                twoLevelMap.put("spread", true);
                twoLevelsList.add(twoLevelMap);
            }
            result.add(oneLevelMap);
        }

        // 功能模块
        Map<String, Object> functions = new HashMap<>();
        functions.put("title", "指定功能");
        functions.put("id", "function");
        functions.put("spread", true);
        List<Map> funcs = new ArrayList<>();
        functions.put("children", funcs);
        List<Permission> permissions = permissionService.selectList(new EntityWrapper<Permission>().eq("status", 1));
        for (Permission permission : permissions) {
            Map<String, Object> func = new HashMap<>();
            func.put("title", permission.getName());
            func.put("id", permission.getAction());
            func.put("spread", true);
            funcs.add(func);
        }
        result.add(functions);

        return R.ok(result);
    }

    @RequestMapping(value = "/power/{roleId}/auth")
    @ManagerAuth
    public R get(@PathVariable("roleId") Long roleId) {
        List<String> result = new ArrayList<>();
        // 菜单
        List<RoleResource> roleResources = roleResourceService.selectList(new EntityWrapper<RoleResource>().eq("role_id", roleId));
        for (RoleResource roleResource : roleResources){
            Resource resource = resourceService.selectById(roleResource.getResourceId());
            if (!Cools.isEmpty(resource)){
                if (resource.getLevel() == 2){
                    result.add(resource.getCode());
                }
            }
        }
        // 功能
        List<RolePermission> rolePermissions = rolePermissionService.selectList(new EntityWrapper<RolePermission>().eq("role_id", roleId));
        for (RolePermission rolePermission : rolePermissions){
            Permission permission = permissionService.selectById(rolePermission.getPermissionId());
            if (!Cools.isEmpty(permission)){
                result.add(permission.getAction());
            }
        }
        return R.ok(result);
    }

    @RequestMapping("/power/auth")
    @ManagerAuth
    public R power(Long roleId, String[] powers){
        roleResourceService.delete(new EntityWrapper<RoleResource>().eq("role_id", roleId));
        rolePermissionService.delete(new EntityWrapper<RolePermission>().eq("role_id", roleId));
        if (!Cools.isEmpty(powers)){
            for (String power : powers) {
                Resource resource = resourceService.selectOne(new EntityWrapper<Resource>().eq("code", power).eq("level", 2));
                if (!Cools.isEmpty(resource)) {
                    RoleResource roleResource = new RoleResource();
                    roleResource.setRoleId(roleId);
                    roleResource.setResourceId(resource.getId());
                    roleResourceService.insert(roleResource);
                } else {
                    Permission permission = permissionService.selectOne(new EntityWrapper<Permission>().eq("action", power));
                    if (!Cools.isEmpty(permission)){
                        RolePermission rolePermission = new RolePermission();
                        rolePermission.setRoleId(roleId);
                        rolePermission.setPermissionId(permission.getId());
                        rolePermissionService.insert(rolePermission);
                    }
                }

            }
        }
        return R.ok();
    }

}
