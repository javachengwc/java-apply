package com.shop.book.manage.user.interceptor;

import com.shop.base.model.Req;
import com.shop.base.model.ReqHeader;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.base.util.JsonUtil;
import com.shop.book.manage.user.autoconfigure.ShopManageUserProperties;
import com.shop.book.manage.user.service.ShopManageUserService;
import com.util.web.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import org.apache.commons.lang.StringUtils;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShopManageInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(ShopManageInterceptor.class);

    public  static  ThreadLocal<String> threadToken = new ThreadLocal<String>();

    public static String sessionCookieKey="session.token";

    @Autowired
    private ShopManageUserProperties shopManageUserProperties;

    @Autowired
    private ShopManageUserService shopManageUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String requesetUri = request.getRequestURI();
        logger.info("ShopManageInterceptor preHandle start,requesetUri={}",requesetUri);

        String token = "";
        Cookie sessionCookie = CookieUtil.getCookie(request,sessionCookieKey);
        if(sessionCookie!=null) {
            token = sessionCookie.getValue();
        }
        if(StringUtils.isBlank(token)) {
            String bodyContent = readBodyContent(request);
            if (StringUtils.isNotBlank(bodyContent)) {
                try {
                    Req req = JsonUtil.json2Obj(bodyContent, Req.class);
                    ReqHeader header = req == null ? null : req.getHeader();
                    token = header == null ? "" : header.getToken();
                    token = token == null ? "" : token;
                } catch (Exception e) {
                    logger.error("ShopManageInterceptor preHandle 提取token异常,bodyContent={}", bodyContent, e);
                }
            }
        }
        if(StringUtils.isNotBlank(token)) {
            //将token放到线程上下文
            threadToken.set(token);
        }

        if (!needAuthLogin(requesetUri)) {
            //不需要登陆验证
            return true;
        }

        Resp<String> resp = new Resp<String>();
        resp.getHeader().setCode(RespHeader.FAIL);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");

        if(StringUtils.isBlank(token)) {
            resp.getHeader().setMsg("登录信息过期,请重新登录");
            response.getWriter().write(JsonUtil.obj2Json(resp));
            return false;
        }
        boolean rt= shopManageUserService.checkUserLogin(token);
        if(!rt) {
            resp.getHeader().setMsg("登录信息过期,请重新登录");
            response.getWriter().write(JsonUtil.obj2Json(resp));
        }
        return rt;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //清除线程上下文中的token
        threadToken.remove();
    }

    public static String readBodyContent(HttpServletRequest request) throws IOException {
        String line = null;
        ServletInputStream inputStream = request.getInputStream();
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        return content.toString();
    }

    public Boolean needAuthLogin(String requestUri) {
        if(StringUtils.isBlank(requestUri)) {
            return false;
        }
        String needAuthLoginPath = shopManageUserProperties.getNeedLoginAuthPath();
        if (StringUtils.isNotBlank(needAuthLoginPath)) {
            String[] needList= needAuthLoginPath.split(",");
            for (String resource : needList) {
                if (requestUri.equals(resource) || requestUri.indexOf(resource) >= 0 ) {
                    return true;
                }
            }
        }
        return false;
    }
}
