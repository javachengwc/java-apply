package com.manage.rbac.context;

import com.manage.rbac.model.dto.UserDTO;
import com.manage.rbac.provider.ITokenProvider;
import com.model.base.Resp;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class LoginContext implements ApplicationContextAware {

    //Spring应用上下文环境
    private static ApplicationContext applicationContext;

    public static String TOKEN_HEADER="token";

    private static LoginContext selfContext;

    @Reference(version = "1.0.0")
    private ITokenProvider tokenProvider;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        LoginContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static  LoginContext getLoginContextBean() {
        if(selfContext==null) {
            selfContext= applicationContext.getBean(LoginContext.class);
        }
        return selfContext;
    }

    //获取登陆态用户信息
    public static UserDTO getLoginUser() {
        LoginContext loginContext = LoginContext.getLoginContextBean();
        UserDTO userDTO = loginContext.getLoginUserByToken();
        return userDTO;
    }

    //检查是否登陆
    public static boolean checkUserLogin() {
        LoginContext loginContext = LoginContext.getLoginContextBean();
        boolean rt = loginContext.checkLogin();
        return rt;
    }

    //获取登陆token
    public static String getUserToken() {
        LoginContext loginContext = LoginContext.getLoginContextBean();
        String token = loginContext.getToken();
        return token;
    }

    public String  getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token =request.getHeader(TOKEN_HEADER);
        return token ;
    }

    public UserDTO  getLoginUserByToken() {
        String token =this.getToken();
        if(null== token || "".equals(token)) {
            return null;
        }
        Resp<UserDTO> rt =tokenProvider.getUserByToken(token);
        UserDTO user = rt.getData();
        return user;
    }

    public boolean  checkLogin() {
        String token =this.getToken();
        if(null== token || "".equals(token)) {
            return false;
        }
        Resp<Boolean> rt =tokenProvider.checkLogin(token);
        Boolean login = rt.getData();
        return login==null?false:login;
    }
}
