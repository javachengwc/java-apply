package com.manage.rbac.shiro;

import com.model.base.Resp;
import com.springdubbo.common.RestCode;
import com.util.JsonUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication(认证)
 */
public class ShiroAuthFilter extends FormAuthenticationFilter {

    private static Logger logger = LoggerFactory.getLogger(ShiroAuthFilter.class);

    //访问被拒绝时候的回调
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //判断请求是否是登录请求
        if (isLoginRequest(request, response)) {
            //判断请求是否是post方法
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            //如果访问的是非登录页面
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            if(httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
                httpResponse.setStatus(HttpStatus.OK.value());
                return true;
            }

            Resp<Void> resp = Resp.error(RestCode.V_NOT_LOGIN);
            String jsonStr = JsonUtil.obj2Json(resp);
            this.renderJson(jsonStr,httpResponse);
            return false;
        }
    }

    private  void renderJson(String json, HttpServletResponse response){
        try {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(json);
            response.flushBuffer();
        } catch (IOException e) {
            logger.error("ShiroAuthFilter renderJSON error,",e);
        }
    }
}
