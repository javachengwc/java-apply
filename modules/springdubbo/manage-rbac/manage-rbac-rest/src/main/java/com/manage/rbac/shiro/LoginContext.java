package com.manage.rbac.shiro;

import com.manage.rbac.DubboFactory;
import com.manage.rbac.model.dto.UserDTO;
import com.manage.rbac.util.SpringContextUtil;
import com.model.base.Resp;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginContext {

    private static String TOKEN_HEADER="token";

    @Autowired
    private DubboFactory dubboFactory;

    private static LoginContext loginContext;

    private final  ThreadLocal<UserDTO> currentUser = new ThreadLocal<UserDTO>();

    public ThreadLocal<UserDTO> getCurrentUser() {
        return currentUser;
    }

    //0--自生登陆 1--接平台登陆
    private static int loginFlag=0;

    public static  LoginContext getLoginContextBean() {
        if(loginContext==null) {
            loginContext= SpringContextUtil.getBean(LoginContext.class);
        }
        return loginContext;
    }

    public static UserDTO getLoginUser() {
        if(loginFlag==0) {
            UserDTO userDTO = LoginContext.getLoginUserWithSelf();
            return userDTO;
        }
        UserDTO userDTO = LoginContext.getLoginUserWithPlat();
        return userDTO;
    }

    public static UserDTO getLoginUserWithSelf() {
        UserDTO userDTO = loginContext.getCurrentUser().get();
        if(userDTO==null) {
            Subject subject = SecurityUtils.getSubject();
            if (subject != null) {
                Object principal = subject.getPrincipal();
                if (!(principal instanceof UserDTO)) {
                    return null;
                }
                userDTO = (UserDTO) principal;
                loginContext.getCurrentUser().set(userDTO);
            }
        }
        return userDTO;
    }

    public static UserDTO getLoginUserWithPlat() {
        LoginContext loginContext = LoginContext.getLoginContextBean();
        UserDTO userDTO = loginContext.getCurrentUser().get();
        if(userDTO==null) {
            userDTO = loginContext.getLoginUserPlat();
            if(userDTO!=null) {
                loginContext.getCurrentUser().set(userDTO);
            }
        }
        return userDTO;
    }

    public static boolean clearCurrentUser() {
        LoginContext loginContext = LoginContext.getLoginContextBean();
        loginContext.getCurrentUser().remove();
        return true;
    }

    //企业微信扫码绑定平台账号的登陆方式获取登陆信息
    public UserDTO  getLoginUserPlat() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token =request.getHeader(TOKEN_HEADER);
        if(StringUtils.isBlank(token)) { 
            return null;
        }
        Resp<UserDTO> rt =dubboFactory.getTokenProvider().getUserByToken(token);
        UserDTO user = rt.getData();
        return user;
    }
}
