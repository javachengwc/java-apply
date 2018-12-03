package com.shop.book.manage.service.rdbc.impl;

import com.shop.base.model.Resp;
import com.shop.book.manage.enums.ApiCode;
import com.shop.book.manage.model.pojo.User;
import com.shop.book.manage.model.vo.LoginResultVo;
import com.shop.book.manage.model.vo.LoginVo;
import com.shop.book.manage.service.rdbc.LoginService;
import com.shop.book.manage.service.rdbc.UserService;
import com.shop.book.manage.shiro.ShiroConstant;
import com.shop.book.manage.shiro.ShiroManager;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private static Logger logger= LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private ShiroManager shiroManager;

    @Autowired
    private UserService userService;

    //登录
    public Resp<LoginResultVo> login(LoginVo loginVo) {
        String mobile =loginVo.getMobile();
        String captcha=loginVo.getCaptcha();
        logger.info("LoginServiceImpl login start,mobile={},captcha={}",mobile,captcha);

        User user = userService.queryByMobile(mobile);
        if(user==null) {
            return Resp.error(ApiCode.ACCOUNT_UNKOWN.getCode(),ApiCode.ACCOUNT_UNKOWN.getMessage());
        }
        Resp<Session> sessionResp = shiroManager.login(mobile,captcha);
        if (!sessionResp.isSuccess()) {
            //登录失败
            return Resp.error(sessionResp);
        }

        LoginResultVo loginResult = new LoginResultVo();
        loginResult.setUserId(user.getId());
        loginResult.setMobile(mobile);
        loginResult.setName(user.getName());

        Session session = sessionResp.getData();
        session.setAttribute(ShiroConstant.SHIRO_SESSION_USER_KEY, mobile);
        session.setAttribute(ShiroConstant.SHIRO_SESSION_USER_ROLES_KEY, loginResult.getRoles());
        String token =session.getId().toString();
        loginResult.setToken(token);

        return Resp.success(loginResult);
    }

    //登出
    public boolean logout() {
        shiroManager.logout();
        return true;
    }
}
