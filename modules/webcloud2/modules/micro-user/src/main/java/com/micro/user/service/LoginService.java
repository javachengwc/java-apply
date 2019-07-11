package com.micro.user.service;

import com.micro.user.model.pojo.User;
import com.micro.user.model.req.LoginReq;
import com.micro.user.model.vo.LoginVo;

public interface LoginService {

    //登录
    public LoginVo login(User user, LoginReq loginReq);

    //登出
    public boolean logout(Long userId);

    //检查登录态
    public boolean checkLogin(Long userId);

    //根据token检查用户登录状态
    public boolean checkLoginByToken(Long userId,String token);

    //刷新用户token
    public boolean refreshToken(Long userId);
}
