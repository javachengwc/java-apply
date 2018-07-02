package com.shop.order.service.remote;


import com.shop.base.model.Resp;
import com.shop.user.api.model.UserInfo;

public interface UserService {

    public UserInfo getUserInfo(Long userId);

    public Resp<UserInfo> getUserInfo2(Long userId);

    public Resp<UserInfo> queryUserInfo3(Long userId);
}
