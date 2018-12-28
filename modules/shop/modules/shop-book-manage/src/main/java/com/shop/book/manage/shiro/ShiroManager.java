package com.shop.book.manage.shiro;

import com.shop.base.model.Resp;
import com.shop.book.manage.enums.ApiCode;
import com.shop.book.manage.model.pojo.manage.User;
import com.shop.book.manage.service.rdbc.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ShiroManager {

    private static final Logger logger = LoggerFactory.getLogger(ShiroManager.class);

    @Autowired
    private SessionDAO sessionDAO;

    @Resource
    private UserService userService;

    //在线用户
    private static ConcurrentMap<String, String> onlineUserMap = new ConcurrentHashMap<String, String>();

    public boolean addOnlineUser(String username, Session session) {
        logger.info("ShiroManager addOnlineUser start, username={},sessionId={}", username, session.getId());
        if (StringUtils.isNotBlank(username)) {
            onlineUserMap.put(username, session.getId().toString());
            return true;
        }
        return false;
    }

    public boolean delOnlineUser(String username) {
        logger.info("ShiroManager delOnlineUser, username={}", username);
        if(StringUtils.isBlank(username)) {
            return true;
        }
        String sessionId = onlineUserMap.remove(username);
        if (StringUtils.isNotBlank(sessionId)) {
            try {
                Session session = sessionDAO.readSession(sessionId);
                if (session != null) {
                    sessionDAO.delete(session);
                }
            } catch (UnknownSessionException e) {
                logger.error("ShiroManager delOnlineUser readSession error,username={},sessionId={}",username,sessionId, e);
                return false;
            }
        }
        return true;
    }

    //登录
    public Resp<Session> login(String username, String password) {
        Resp<Session> result = new Resp<Session>();
        //创建令牌,登录的过程被抽象为Shiro验证令牌是否具有合法身份以及相关权限
        UsernamePasswordToken token  = new UsernamePasswordToken(username, password);
        //项目,代表的就是此应用项目
        Subject subject = SecurityUtils.getSubject();
        try {
            //令牌（token）与项目（subject）的登陆（login）
            subject.login(token );
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            return Resp.error(ApiCode.ACCOUNT_UNKOWN.getCode(),ApiCode.ACCOUNT_UNKOWN.getMessage());
        } catch (LockedAccountException ee) {
            return Resp.error(ApiCode.ACCOUNT_LOCKED.getCode(),ApiCode.ACCOUNT_LOCKED.getMessage());
        } catch (ExcessiveAttemptsException eae) {
            return Resp.error(ApiCode.LOGIN_OVER_LIMIT.getCode(),ApiCode.LOGIN_OVER_LIMIT.getMessage());
        } catch (AuthenticationException ae) {
            return Resp.error(ApiCode.UNKOWN_FAIL.getCode(),ApiCode.UNKOWN_FAIL.getMessage());
        }
        Session session = subject.getSession(false);
        addOnlineUser(username, session);
        return result.success(session);
    }

    //登出
    public boolean logout() {
        User user = getLoginUser();
        if (user != null) {
            delOnlineUser(user.getMobile());
        }
        SecurityUtils.getSubject().logout();
        return true;
    }

    public User getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        User user = null;
        if (subject != null) {
            Object principal = subject.getPrincipal();
            if (!(principal instanceof User)) {
                return null;
            }
            user = (User) principal;
        }
        return user;
    }

}
