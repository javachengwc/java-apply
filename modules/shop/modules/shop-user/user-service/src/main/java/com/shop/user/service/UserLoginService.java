package com.shop.user.service;

import com.shop.user.model.ClientDevice;
import com.shop.user.model.pojo.ShopUser;
import com.shop.user.model.pojo.UserLogin;

public interface UserLoginService {

    public UserLogin recordUserLogin(ShopUser user,ClientDevice clientDevice);

    public UserLogin recordUserLogout(Long userId,ClientDevice clientDevice);

    public UserLogin queryLatestLoginByUserId(Long userId);
}
