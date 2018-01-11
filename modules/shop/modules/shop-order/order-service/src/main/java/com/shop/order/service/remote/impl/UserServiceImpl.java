package com.shop.order.service.remote.impl;


import com.shop.order.service.remote.UserService;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.model.base.Rep;
import com.shop.user.api.model.base.Req;
import com.shop.user.api.rest.UserResCtrl;
import com.shop.user.api.rest.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserResCtrl userResCtrl;

    public UserInfo getUserInfo(Long userId) {
        return userResCtrl.getUserInfo(userId);
    }


    public Rep<UserInfo> getUserInfo2(Long userId) {
        return userResCtrl.getUserInfo2(userId);
    }

    public Rep<UserInfo> queryUserInfo3(Long userId) {
        Req<Long> req = new Req<Long>();
        req.setData(userId);
        return userResCtrl.queryUserInfo3(req,null);
    }
}
