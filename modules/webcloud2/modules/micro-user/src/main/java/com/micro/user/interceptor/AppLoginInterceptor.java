package com.micro.user.interceptor;

import com.micro.user.annotation.AppLogin;
import com.micro.user.service.LoginService;
import com.shop.base.model.Resp;
import com.shop.base.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

//app端登录验证
@Order(10)
public class AppLoginInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AppLoginInterceptor.class);

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String reqUri = request.getRequestURI();
        logger.info("AppLoginInterceptor preHandle app登录校验开始,reqUri={}",reqUri);

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        AppLogin appLogin = method.getAnnotation(AppLogin.class);

        if (appLogin == null) {
            //不需要登录验证
            return true;
        }

        String token="";//从body或者header中获取token
        if (StringUtils.isNotEmpty(token)) {
            boolean checkResult= loginService.checkLoginByToken(token);
            if(checkResult) {
                return true;
            }
        }
        Resp<String> resp =Resp.error("请先登录");

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
