package com.micro.user.interceptor;

import com.micro.user.annotation.AppPermission;
import com.shop.base.model.Resp;
import com.shop.base.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

//app权限认证
@Order(20)
public class AppAuthInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AppAuthInterceptor.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String reqUri = request.getRequestURI();
        logger.info("AppAuthInterceptor preHandle app权限认证开始,reqUri={}",reqUri);

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        AppPermission appPermission = method.getAnnotation(AppPermission.class);

        if (appPermission == null) {
            //不需要权限认证
            return true;
        }

        String token = "";//从body或者header中获取token
        if (StringUtils.isEmpty(token)) {
            Resp<String> resp =Resp.error("请先登录");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.getWriter().write(JsonUtil.obj2Json(resp));
            return false;
        }

        Object appPerms = redisTemplate.opsForHash().get(token, "appPerms");
        if (appPerms != null) {
            String permsStr = appPerms.toString();
            String[] perms = StringUtils.split(permsStr, ",");
            if (perms != null && perms.length > 0) {
                List<String> permsList = Arrays.asList(perms);
                if (permsList.contains(appPermission.value())) {
                    return true;
                }
            }
        }
        Resp<String> resp =Resp.error("无权限访问");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.getWriter().write(JsonUtil.obj2Json(resp));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}