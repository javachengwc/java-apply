package com.shop.book.manage.user.service.impl;

import com.shop.base.model.Req;
import com.shop.base.model.ReqHeader;
import com.shop.base.model.Resp;
import com.shop.base.util.JsonUtil;
import com.shop.book.manage.user.autoconfigure.ShopManageUserProperties;
import com.shop.book.manage.user.interceptor.ShopManageInterceptor;
import com.shop.book.manage.user.model.ShopManageUser;
import com.shop.book.manage.user.service.ShopManageUserService;
import com.util.http.HttpClientUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ShopManageUserImpl implements ShopManageUserService {

    private static Logger logger = LoggerFactory.getLogger(ShopManageUserImpl.class);

    //获取登陆用户信息
    private static String GET_LOGIN_PATH = "/getLogin";

    //查询用户信息
    private static String QUERY_USER_PATH = "/user/queryUser";

    //检查用户是否登陆
    private static String CHECK_USER_LOGIN= "/checkLogin";

    @Autowired
    private ShopManageUserProperties shopManageUserProperties;

    @Override
    public ShopManageUser getLoginUser() {
        String token = ShopManageInterceptor.threadToken.get();
        if (StringUtils.isBlank(token)) {
            logger.info("ShopManageUserImpl getLoginUser token is null...");
            return null;
    }
        ShopManageUser user = getLoginUser(token);
        return user;
    }

    @Override
    public ShopManageUser getLoginUser(String token) {
        String url = shopManageUserProperties.getServerUrl() + GET_LOGIN_PATH;
        Req<Void> req = new Req<Void>();
        req.setHeader(new ReqHeader());
        req.getHeader().setToken(token);
        String params = JsonUtil.obj2Json(req);
        Resp<ShopManageUser> resps = null;
        logger.info("ShopManageUserImpl getLoginUser begin invoke remote service ,url={}", url);
        try {
            String jsonResult = HttpClientUtil.post(url, params);
            if (StringUtils.isNotBlank(jsonResult)) {
                resps = JsonUtil.json2Obj(jsonResult, Resp.class, ShopManageUser.class);
            } else {
                logger.info("ShopManageUserImpl getLoginUser 远程调用jsonResult is null ");
            }
        } catch (Exception e) {
            logger.error("ShopManageUserImpl getLoginUser 远程调用异常", e);
        }
        ShopManageUser user = resps == null ? null : resps.getData();
        Integer hasUser = user == null ? 0 : 1;
        Long userId = user == null ? null : user.getId();
        String userName = user == null ? null : user.getName();
        logger.info("ShopManageUserImpl getLoginUser  远程调用完成,hasUser={},userId={},userName={}", hasUser, userId, userName);
        return user;
    }

    //检查用户是否登陆
    @Override
    public boolean checkUserLogin() {
        String token = ShopManageInterceptor.threadToken.get();
        if (StringUtils.isBlank(token)) {
            logger.info("ShopManageUserImpl checkUserLogin token is null...");
            return false;
        }
        boolean rt = checkUserLogin(token);
        return rt;
    }

    @Override
    public boolean checkUserLogin(String token) {
        String url = shopManageUserProperties.getServerUrl() + CHECK_USER_LOGIN;
        Req<Void> req = new Req<Void>();
        req.setHeader(new ReqHeader());
        req.getHeader().setToken(token);
        String params = JsonUtil.obj2Json(req);
        Resp<Integer> resps = null;
        logger.info("ShopManageUserImpl checkUserLogin begin invoke remote service ,url={}", url);
        try {
            String jsonResult = HttpClientUtil.post(url, params);
            if (StringUtils.isNotBlank(jsonResult)) {
                resps = JsonUtil.json2Obj(jsonResult, Resp.class, Integer.class);
            } else {
                logger.info("ShopManageUserImpl checkUserLogin 远程调用jsonResult is null ");
            }
        } catch (Exception e) {
            logger.error("ShopManageUserImpl checkUserLogin 远程调用异常", e);
        }
        Integer loginFlag = resps == null ? null : resps.getData();
        boolean rt = (loginFlag != null && loginFlag == 1);
        logger.info("ShopManageUserImpl checkUserLogin  远程调用完成,loginFlag={}", loginFlag);
        return rt;
    }

    //查询用户信息
    @Override
    public ShopManageUser queryById(Long id) {
        String url = shopManageUserProperties.getServerUrl() + QUERY_USER_PATH;
        Req<Long> req = new Req<Long>();
        req.setHeader(new ReqHeader());
        req.setData(id);
        String params = JsonUtil.obj2Json(req);
        Resp<ShopManageUser> resp = null;
        logger.info("ShopManageUserImpl queryById begin invoke remote service ,url={},params={}", url, params);
        try {
            String jsonResult = HttpClientUtil.post(url, params);
            if (StringUtils.isNotBlank(jsonResult)) {
                resp = JsonUtil.json2Obj(jsonResult, Resp.class, ShopManageUser.class);
            } else {
                logger.info("ShopManageUserImpl queryById 远程调用jsonResult is null ");
            }
        } catch (Exception e) {
            logger.error("ShopManageUserImpl queryById 远程调用异常", e);
        }
        ShopManageUser user = resp == null ? null : resp.getData();
        Integer hasUser = user == null ? 0 : 1;
        Long userId = user == null ? null : user.getId();
        String userName = user == null ? null : user.getName();
        logger.info("ShopManageUserImpl queryById 远程调用完成,hasUser={},userId={},userName={}", hasUser, userId, userName);
        return user;
    }
}
