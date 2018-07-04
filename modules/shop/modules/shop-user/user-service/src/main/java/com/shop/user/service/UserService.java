package com.shop.user.service;

import com.shop.base.model.Resp;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.model.vo.LoginVo;
import com.shop.user.model.ClientDevice;
import com.shop.user.model.pojo.ShopUser;

public interface UserService {

    public ShopUser getById(Long userId);

    public UserInfo getUserInfo(Long userId);

    public ShopUser queryByMobile(String mobile);

    public Resp<UserInfo> login(LoginVo loginVo, ClientDevice clientDevice);

    public boolean logout(String token,ClientDevice clientDevice);

    public Resp<Void> checkLogin(String token);

    //根据token获取登录态用户Id
    public Long getLoginUserId(String token);
}
