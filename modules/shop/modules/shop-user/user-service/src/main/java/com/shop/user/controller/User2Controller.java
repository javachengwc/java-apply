package com.shop.user.controller;

import com.shop.user.api.model.UserInfo;
import com.shop.user.api.model.base.Rep;
import com.shop.user.api.model.base.Req;
import com.shop.user.api.rest.User2Resource;
import com.shop.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class User2Controller implements User2Resource {

    private static Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    public UserInfo getUserInfo(Long userId){
        logger.info("User2Controller getUserInfo start,userId={}",userId);
        if(userId==null) {
            return null;
        }
        return userService.getUserInfo(userId);
    }

    public Rep<UserInfo> getUserInfo2(Long userId) {
        logger.info("User2Controller getUserInfo2 start,userId={}",userId);

        Rep<UserInfo> rep= new Rep<UserInfo>();
        if(userId==null) {
            rep.getHeader().setRet("F");
            return rep;
        }
        UserInfo userInfo = userService.getUserInfo(userId);
        rep.setData(userInfo);
        rep.getHeader().setRet("S");
        return rep;
    }

    public Rep<UserInfo> queryByCdn(@RequestBody Req<UserInfo> req, Errors errors){
        logger.info("User2Controller queryByCdn start,req={}",req);
        Rep<UserInfo> rep = new Rep<UserInfo>();
        if(req==null || req.getData()==null) {
            rep.getHeader().setRet("F");
            return rep;
        }
        Long userId = req.getData().getId();
        if(userId==null) {
            rep.getHeader().setRet("F");
            return rep;
        }
        UserInfo userInfo = userService.getUserInfo(userId);
        rep.setData(userInfo);
        rep.getHeader().setRet("S");
        return rep;
    }
}
