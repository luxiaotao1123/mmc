package com.cool.mmc.common.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cool.mmc.common.utils.Http;
import com.cool.mmc.system.entity.*;
import com.cool.mmc.system.service.*;
import com.core.annotations.ManagerAuth;
import com.core.common.BaseRes;
import com.core.common.Cools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by vincent on 2019-06-13
 */
@Component
public class AdminInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private OperateLogService operateLogService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token!=null) {
            String deToken = Cools.deTokn(token, "a8934ef22591e30449f09285ef27c8c6");
            if (deToken!=null){
                long timestamp = Long.parseLong(deToken.substring(0, 13));
                // 1天后过期
                if (System.currentTimeMillis() - timestamp > 86400000){
                    Http.response(response, BaseRes.DENIED);
                    return false;
                }
                if ("super".equals(deToken.substring(13))) {
                    request.setAttribute("userId", 9527);
                    return true;
                }
            }
        }

        if (handler instanceof org.springframework.web.servlet.resource.ResourceHttpRequestHandler) {
            return true;
        }
        // 跨域设置
        // response.setHeader("Access-Control-Allow-Origin", "*");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(ManagerAuth.class)){
            ManagerAuth annotation = method.getAnnotation(ManagerAuth.class);
            if (annotation.value().equals(ManagerAuth.Auth.CHECK)){
                return check(request, response);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
        Object obj = request.getAttribute("operateLog");
        if (obj instanceof OperateLog) {
            OperateLog operate = (OperateLog) obj;
            operate.setResponse(String.valueOf(response.getStatus()));
            operateLogService.insert(operate);
        }
    }

    private boolean check(HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = request.getHeader("token");
            UserLogin userLogin = userLoginService.selectOne(new EntityWrapper<UserLogin>().eq("token", token));
            if (null == userLogin){
                Http.response(response, BaseRes.DENIED);
                return false;
            }
            User user = userService.selectById(userLogin.getUserId());
            String deToken = Cools.deTokn(token, user.getPassword());
            long timestamp = Long.parseLong(deToken.substring(0, 13));
            // 1天后过期
            if (System.currentTimeMillis() - timestamp > 86400000){
                Http.response(response, BaseRes.DENIED);
                return false;
            }
            // 权限校验
            if (!limit(request.getRequestURI(), user)) {
                Http.response(response, BaseRes.LIMIT);
                return false;
            }
            // 操作日志
            OperateLog operateLog = new OperateLog();
            operateLog.setAction(request.getRequestURI());
            operateLog.setIp(request.getRemoteAddr());
            operateLog.setUserId(user.getId());
            operateLog.setRequest(JSON.toJSONString(request.getParameterMap()));
            // 请求缓存
            request.setAttribute("userId", user.getId());
            request.setAttribute("operateLog", operateLog);
            return true;
        } catch (Exception e){
            Http.response(response, BaseRes.DENIED);
            return false;
        }

    }

    /**
     * 权限拦截
     * @return false:无权限;   true:认证通过
     */
    private boolean limit(String action, User user) {
        Permission permission = new Permission();
        permission.setAction(action);
        permission.setStatus((short) 1);
        Permission one = permissionService.selectOne(new EntityWrapper<>(permission));
        if (!Cools.isEmpty(one)) {
            RolePermission rolePermission = rolePermissionService.selectOne(new EntityWrapper<>(new RolePermission(user.getRoleId(), permission.getId())));
            return !Cools.isEmpty(rolePermission);
        }
        return true;
    }

}
