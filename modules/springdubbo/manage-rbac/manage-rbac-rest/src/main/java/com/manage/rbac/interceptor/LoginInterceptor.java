package com.manage.rbac.interceptor;

import com.manage.rbac.model.dto.UserDTO;
import com.manage.rbac.model.enums.EnableEnum;
import com.manage.rbac.shiro.LoginContext;
import com.model.base.Resp;
import com.springdubbo.common.RestCode;
import com.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(-1)
public class LoginInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String reqUri = request.getRequestURI();
        logger.info("LoginInterceptor preHandle start,reqUri={}",reqUri);

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if(reqUri.indexOf("/rbac")<0) {
            return true;
        }
        logger.info("LoginInterceptor preHandle 校验开始,reqUri={}",reqUri);
        UserDTO user = LoginContext.getLoginUser();
        if(user==null) {
            Resp<Void> resp = Resp.error(RestCode.V_NOT_LOGIN);
            String jsonStr = JsonUtil.obj2Json(resp);
            this.renderJson(jsonStr,response);
            return false;
        }
        if(user.getState() == EnableEnum.FORBID.getValue()) {
            LoginContext.clearCurrentUser();
            Resp<Void> resp = Resp.error(RestCode.V_NOT_LOGIN);
            String jsonStr = JsonUtil.obj2Json(resp);
            this.renderJson(jsonStr,response);
            return false;
        }
        return true;
    }

    private  void renderJson(String json, HttpServletResponse response){
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
            response.flushBuffer();
        } catch (IOException e) {
            logger.error("LoginInterceptor renderJSON error,",e);
        }
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        logger.info("LoginInterceptor postHandle start,clearCurrentUser");
        LoginContext.clearCurrentUser();
    }

}
