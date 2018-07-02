package com.shop.order.service.remote.impl;


import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.order.service.remote.UserService;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.rest.test.UserResCtrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserResCtrl userResCtrl;

    public UserInfo getUserInfo(Long userId) {
        return userResCtrl.getUserInfo(userId);
    }


    public Resp<UserInfo> getUserInfo2(Long userId) {
        return userResCtrl.getUserInfo2(userId);
    }

    public Resp<UserInfo> queryUserInfo3(Long userId) {
        Req<Long> req = new Req<Long>();
        req.setData(userId);
        return userResCtrl.queryUserInfo3(req,null);
    }
}
