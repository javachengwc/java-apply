package com.storefront.manage.service.impl;

import com.shop.base.model.Resp;
import com.storefront.manage.enums.ApiCode;
import com.storefront.manage.model.pojo.rbac.User;
import com.storefront.manage.model.vo.LoginResultVo;
import com.storefront.manage.model.vo.LoginVo;
import com.storefront.manage.model.vo.MenuVo;
import com.storefront.manage.model.vo.RoleVo;
import com.storefront.manage.service.LoginService;
import com.storefront.manage.service.rbac.MenuService;
import com.storefront.manage.service.rbac.RoleService;
import com.storefront.manage.service.rbac.UserService;
import com.storefront.manage.shiro.ShiroConstant;
import com.storefront.manage.shiro.ShiroManager;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    private static Logger logger= LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private ShiroManager shiroManager;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

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

        Long userId =user.getId();
        LoginResultVo loginResult = new LoginResultVo();
        loginResult.setUserId(userId);
        loginResult.setMobile(mobile);
        loginResult.setName(user.getName());

        Session session = sessionResp.getData();
        session.setAttribute(ShiroConstant.SHIRO_SESSION_USER_KEY, mobile);
        session.setAttribute(ShiroConstant.SHIRO_SESSION_USER_ROLES_KEY, loginResult.getRoles());
        String token =session.getId().toString();
        loginResult.setToken(token);

        //菜单
        List<MenuVo> menuList= menuService.queryUserMenu(userId);
        loginResult.setMenus(menuList);

        //角色
        List<RoleVo> roleList = roleService.queryUserRole(userId);
        loginResult.setRoles(roleList);

        return Resp.success(loginResult);
    }

    //登出
    public boolean logout() {
        shiroManager.logout();
        return true;
    }
}
