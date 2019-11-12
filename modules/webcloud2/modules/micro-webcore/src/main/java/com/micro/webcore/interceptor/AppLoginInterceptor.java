package com.micro.webcore.interceptor;

import com.micro.webcore.annotation.AppLogin;
import com.micro.webcore.annotation.InnerResource;
import com.micro.webcore.constant.HeaderConstant;
import com.micro.webcore.constant.JwtConstant;
import com.micro.webcore.feign.LoginCheckService;
import com.micro.webcore.model.LoginUser;
import com.micro.webcore.model.TokenInfo;
import com.micro.webcore.service.ITokenService;
import com.micro.webcore.util.SpringContextUtil;
import com.micro.webcore.util.TokenUtil;
import com.model.base.Req;
import com.model.base.Resp;
import com.util.JsonUtil;
import com.util.date.DateUtil;
import com.util.web.HttpRenderUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

public class AppLoginInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(AppLoginInterceptor.class);

    private ITokenService tokenService=null;

    private boolean loadTokenService=false;

    @Autowired
    private LoginCheckService loginCheckService;

    public ITokenService getTokenService() {
        if(loadTokenService) {
            return tokenService;
        }
        try {
            tokenService = SpringContextUtil.getBean(ITokenService.class);
        } catch (Exception e) {
            logger.info("AppLoginInterceptor getTokenService error,不存在ITokenService类型的bean对象");
        }
        loadTokenService=true;
        return tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String reqUri = request.getRequestURI();
        logger.info("AppLoginInterceptor preHandle app登录校验开始,reqUri={}",reqUri);

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //是否内部调用
        boolean comefromFeign=this.comefromFeign(request);
        InnerResource innerResource = method.getAnnotation(InnerResource.class);
        if(innerResource!=null && !comefromFeign ) {
            logger.warn("AppLoginInterceptor preHandle 内部接口非内部调用,reqUri={},methodName={}",reqUri,method.getName());
            HttpRenderUtil.renderJSON(JsonUtil.obj2Json(Resp.error("非法调用")),response);
            return false ;
        }

        AppLogin appLogin = method.getAnnotation(AppLogin.class);
        if (appLogin == null) {
            //不需要登录验证
            return true;
        }
        TokenInfo tokenInfo= TokenUtil.tipTokenInfo(request);
        if(tokenInfo==null || !tokenInfo.hasCorrectToken() || tokenInfo.isExpired() ) {
            logger.warn("AppLoginInterceptor preHandle Authorization header is null or  token is expired ");
            HttpRenderUtil.renderJSON(JsonUtil.obj2Json(Resp.error(1,"用户登录过期")),response);
            return false;
        }
        Long userId = tokenInfo.getUserId();
        String token = tokenInfo.getToken();

        ITokenService tokenService= this.getTokenService();
        if(tokenService!=null) {
            //自身是用户服务
            logger.info("AppLoginInterceptor preHandle 用户服务登录验证..............");
            boolean checkResult= tokenService.checkTokenLogin(userId,token);
            if(!checkResult) {
                logger.warn("AppLoginInterceptor preHandle checkTokenLogin not pass");
                HttpRenderUtil.renderJSON(JsonUtil.obj2Json(Resp.error(1,"用户登录过期")),response);
                return false ;
            }
            long diff= DateUtil.getDayDiff(tokenInfo.getExpiredDate(),new Date());
            //过期时间不到3天，重新生成下token
            if(diff<=2) {
                if(comefromFeign) {
                    //内部调用,延期token
                    tokenService.deferToken(userId,token);
                } else {
                    //刷新token
                    String newToken = tokenService.refreshToken(userId);
                    if (!StringUtils.isBlank(newToken)) {
                        response.setHeader(JwtConstant.HEADER_TOKEN, newToken);
                    }
                }
            }
        } else {
            //第三方服务调用用户服务登录验证
            logger.info("AppLoginInterceptor preHandle 第三方服务调用用户服务登录验证..............");
            try {
                Resp<Void> resp = loginCheckService.checkLogin(new Req<Void>());
                if (!resp.isSuccess()) {
                    HttpRenderUtil.renderJSON(JsonUtil.obj2Json(Resp.error(1,"用户登录过期")),response);
                    return false ;
                }
            }catch (Exception e ) {
                logger.warn("AppLoginInterceptor preHandle invoke checkLogin error,",e);
                HttpRenderUtil.renderJSON(JsonUtil.obj2Json(Resp.error(1,"用户登录过期")),response);
                return false ;
            }
        }
        LoginUser user = new LoginUser(userId, tokenInfo.getMobile());
        request.setAttribute("loginUser", user);
        return true;
    }

    //是否来自feign调用
    public boolean comefromFeign(HttpServletRequest request) {
        String comefrom = request.getHeader(HeaderConstant.ACCESS_COMEFROM_HEARDER);
        if (StringUtils.isNotBlank(comefrom) && HeaderConstant.ACCESS_COMEFROM_FEIGN.equals(comefrom)) {
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}