package com.shop.book.manage.shiro;

import com.shop.base.model.Resp;
import com.shop.base.util.JsonUtil;
import com.shop.book.manage.enums.ApiCode;
import com.util.web.HttpRenderUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//Authentication(认证),在web应用中认证是通过FormAuthenticationFilter拦截器来实现的
public class ShiroAuthFilter extends FormAuthenticationFilter {

    private static Logger logger = LoggerFactory.getLogger(ShiroAuthFilter.class);

    //访问被拒绝时候的回调
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //判断请求是否是登录请求
        if (isLoginRequest(request, response)) {
            //判断请求是否是post方法
            if (isLoginSubmission(request, response)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("ShiroAuthFilter onAccessDenied begin to executeLogin()");
                }
                //执行登录验证
                return executeLogin(request, response);
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("ShiroAuthFilter onAccessDenied isLoginSubmission false");
                }
                //如果是get方法则会返回true,跳转到登陆页面
                return true;
            }
        } else {
            //如果访问的是非登录页面
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            if(httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
                //OPTIONS请求的作用主要有两个: 1、获取服务器支持的HTTP请求方法；2、用来检查服务器的性能。
                httpResponse.setStatus(HttpStatus.OK.value());
                return true;
            }

            if (logger.isTraceEnabled()) {
                String curPath= ((HttpServletRequest) request).getRequestURI();
                logger.trace("ShiroAuthFilter onAccessDenied attempting to access a path which requires auth,curPath={}",curPath);
            }

            Resp<Void> resp = Resp.error( ApiCode.NO_LOGIN.getCode(), ApiCode.NO_LOGIN.getMessage());
            String jsonStr = JsonUtil.obj2Json(resp);
            HttpRenderUtil.renderJSON(jsonStr,httpResponse);
            return false;
        }
    }

}
