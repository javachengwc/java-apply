package com.micro.webcore.context;

import com.micro.webcore.model.LoginUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class LoginContext {

    public static LoginUser getLoginUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object obj =request.getAttribute("loginUser");
        if(obj==null) {
            return null;
        }
        LoginUser userVo = (LoginUser)obj;
        return userVo;
    }
}
