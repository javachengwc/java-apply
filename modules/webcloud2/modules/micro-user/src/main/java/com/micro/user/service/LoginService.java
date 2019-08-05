package com.micro.user.service;

import com.micro.user.model.pojo.User;
import com.micro.user.model.req.LoginReq;
import com.micro.user.model.vo.LoginVo;
import com.micro.webcore.service.ITokenService;

public interface LoginService  extends ITokenService {

    //登录
    public LoginVo login(User user, LoginReq loginReq);

    //登出
    public boolean logout(Long userId);

    //检查登录态
    public boolean checkLogin(Long userId);

    //检查token登录
    public boolean checkTokenLogin(Long userId,String token);

    //延期token
    public boolean deferToken(Long userId,String token);

    //刷新token
    public String refreshToken(Long userId);
}
