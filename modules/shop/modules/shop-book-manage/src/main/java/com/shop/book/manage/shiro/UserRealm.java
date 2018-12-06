package com.shop.book.manage.shiro;

import com.shop.book.manage.config.Config;
import com.shop.book.manage.enums.UserStatuEnum;
import com.shop.book.manage.model.pojo.User;
import com.shop.book.manage.service.SmsService;
import com.shop.book.manage.service.rdbc.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

//获取用户的角色和权限信息
public class UserRealm extends AuthorizingRealm{

    private static Logger logger= LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    //AuthorizationInfo代表了角色的权限信息集合
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        Set<String> perms = userService.queryUserPerms(user.getId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(perms);
        return info;
    }

    //AuthenticationInfo代表了用户的角色信息集合
    //提供账户信息返回认证信息
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("UserRealm doGetAuthenticationInfo start,authenticationToken={}",authenticationToken);
        String mobile = (String) authenticationToken.getPrincipal();
        User user = userService.queryByMobile(mobile);
        if (user == null) {
            throw new UnknownAccountException("账号不正确");
        }

        boolean needCapchaCheck=true;//是否需要验证码验证
        String []  profiles =Config.getActiveProfiles();
        if(profiles!=null && Arrays.asList(profiles).contains("dev")) {
            needCapchaCheck=false;
        }
        String captcha = new String((char[]) authenticationToken.getCredentials());
        logger.info("UserRealm doGetAuthenticationInfo mobile={},captcha={}",mobile,captcha);
        if(needCapchaCheck) {
            if (!smsService.verifyCaptcha(mobile, captcha)) {
                throw new IncorrectCredentialsException("验证码验证失败");
            }
        }
        if ( UserStatuEnum.NOMAL.getValue().intValue()!=user.getStatus()) {
            throw new LockedAccountException("账号被冻结");
        }
        //处理session
        SessionsSecurityManager securityManager = (SessionsSecurityManager) SecurityUtils.getSecurityManager();
        DefaultSessionManager sessionManager = (DefaultSessionManager) securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取已登录的所有用户session列表
        for (Session session : sessions) {
            //如果和当前session是同一个session，则不剔除
            if (SecurityUtils.getSubject().getSession().getId().equals(session.getId())) {
                continue;
            }
            //清除该用户以前登录时保存的session
            String  loginedMobile = (String) (session.getAttribute(ShiroConstant.SHIRO_SESSION_USER_KEY));
            if (loginedMobile != null && loginedMobile.equals(mobile)) {
                logger.info("UserRealm doGetAuthenticationInfo 剔除以前的登录态，mobile={},preSessionId={}",mobile,session.getId());
                sessionManager.getSessionDAO().delete(session);
            }
        }

        return new SimpleAuthenticationInfo(user, captcha, getName());
    }
}
