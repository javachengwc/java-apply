package com.manage.rbac.shiro;

import com.manage.rbac.model.dto.UserDTO;
import com.model.base.Resp;
import com.springdubbo.common.RestCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShiroManager {

    private static final Logger logger = LoggerFactory.getLogger(ShiroManager.class);

    @Autowired
    private SessionDAO sessionDAO;

    //登录
    public Resp<Session> login(String username, String password) {
        logger.info("ShiroManager login start,username={},password={}",username,password);
        //创建令牌
        UsernamePasswordToken token  = new UsernamePasswordToken(username, password);
        //项目
        Subject subject = SecurityUtils.getSubject();
        try {
            //令牌（token）与项目（subject）的登陆（login）
            subject.login(token );
        } catch ( AuthenticationException e) {
            return Resp.error(RestCode.V_NOT_LOGIN);
        }
        Session session = subject.getSession(false);
        session.setAttribute(ShiroConstant.SHIRO_SESSION_USER_KEY, username);
        return Resp.data(session);
    }

    //登出
    public boolean logout() {
        SecurityUtils.getSubject().logout();
        return true;
    }

    public UserDTO getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        UserDTO user = null;
        if (subject != null) {
            Object principal = subject.getPrincipal();
            if (!(principal instanceof UserDTO)) {
                return null;
            }
            user = (UserDTO) principal;
        }
        return user;
    }

}
