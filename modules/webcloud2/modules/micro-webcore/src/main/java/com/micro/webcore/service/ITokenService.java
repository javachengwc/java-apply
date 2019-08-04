package com.micro.webcore.service;

public interface ITokenService {

    //检查token登录
    public boolean checkTokenLogin(Long userId,String token);

    //延期token
    public boolean deferToken(Long userId,String token);

    //刷新token
    public String refreshToken(Long userId);

}
