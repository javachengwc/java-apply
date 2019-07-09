package com.micro.user.service;

public interface SmsService {

    //验证验证码
    public boolean checkCode(String mobile,String code);
}
