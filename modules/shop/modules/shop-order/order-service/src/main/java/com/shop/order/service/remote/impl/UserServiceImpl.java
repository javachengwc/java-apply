package com.shop.order.service.remote.impl;


import com.shop.order.service.remote.UserService;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.model.base.Rep;
import com.shop.user.api.rest.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserResource userResource;

    public UserInfo getUserInfo(Long userId) {
        return userResource.getUserInfo(userId);
    }


    public Rep<UserInfo> getUserInfo2(Long userId) {
        return userResource.getUserInfo2(userId);
    }
}
