package com.micro.webcore.context;

import com.micro.webcore.model.LoginUser;
import com.micro.webcore.model.TokenInfo;
import com.micro.webcore.util.TokenUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class LoginContext {

    public static LoginUser getLoginUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object obj =request.getAttribute("loginUser");
        if(obj==null) {
            TokenInfo tokenInfo= TokenUtil.tipTokenInfo(request);
            if(tokenInfo==null || !tokenInfo.hasCorrectToken() ||tokenInfo.isExpired() ) {
                return null;
            }
            LoginUser user = new LoginUser(tokenInfo.getUserId(),tokenInfo.getMobile());
            return user;
        }
        LoginUser user = (LoginUser)obj;
        return user;
    }
}
