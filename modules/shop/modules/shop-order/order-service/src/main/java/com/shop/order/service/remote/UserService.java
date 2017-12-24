package com.shop.order.service.remote;


import com.shop.user.api.model.UserInfo;
import com.shop.user.api.model.base.Rep;

public interface UserService {

    public UserInfo getUserInfo(Long userId);

    public Rep<UserInfo> getUserInfo2(Long userId);
}
