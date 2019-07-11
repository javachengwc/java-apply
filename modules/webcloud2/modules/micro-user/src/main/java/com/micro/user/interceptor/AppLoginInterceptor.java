package com.micro.user.interceptor;

import com.micro.user.annotation.AppLogin;
import com.micro.user.constant.JwtConstant;
import com.micro.user.model.LoginUser;
import com.micro.user.service.LoginService;
import com.micro.user.util.JwtTokenUtil;
import com.shop.base.model.Resp;
import com.shop.base.util.JsonUtil;
import com.util.date.DateUtil;
import com.util.web.HttpRenderUtil;
import io.jsonwebtoken.Claims;
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
import java.util.Date;

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

        String headerTokenValue = request.getHeader(JwtConstant.HEADER_TOKEN);
        if (StringUtils.isBlank(headerTokenValue)) {
            logger.warn("AppLoginInterceptor preHandle token header value is null");
            HttpRenderUtil.renderJSON(JsonUtil.obj2Json(Resp.error(401, "用户登录过期")),response);
            return false;
        }
        Claims claims = JwtTokenUtil.getClaimFromToken(headerTokenValue);
        Date expiredDate = claims.getExpiration();
        Date now = new Date();
        //验证token是否过期
        if (expiredDate.before(now)) {
            logger.warn("AppLoginInterceptor preHandle token is expired");
            HttpRenderUtil.renderJSON(JsonUtil.obj2Json(Resp.error(401, "用户登录过期")),response);
            return false;
        }
        Long userId = Long.valueOf(claims.getId());
        boolean checkResult= loginService.checkLoginByToken(userId,headerTokenValue);
        if(!checkResult) {
            logger.warn("AppLoginInterceptor preHandle token not login");
            HttpRenderUtil.renderJSON(JsonUtil.obj2Json(Resp.error(401, "用户登录过期")),response);
            return false ;
        }
        long diff= DateUtil.getDayDiff(expiredDate,now);
        if(diff<=2 ) {
            //过期时间不到3天，重新生成下token
            loginService.refreshToken(userId);
        }
        LoginUser loginUser = new LoginUser(userId, claims.getSubject());
        request.setAttribute("loginUser", loginUser);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
