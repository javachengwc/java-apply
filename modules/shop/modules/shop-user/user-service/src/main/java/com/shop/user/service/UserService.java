package com.shop.user.service;

import com.shop.user.api.model.UserInfo;
import com.shop.user.model.LoginInfo;
import com.shop.user.model.pojo.ShopUser;

public interface UserService {

    public ShopUser getById(Long userId);

    public UserInfo getUserInfo(Long userId);

    public UserInfo  login(LoginInfo loginInfo);

    public boolean logout(String token);

    public boolean checkLogin(String token);
}
