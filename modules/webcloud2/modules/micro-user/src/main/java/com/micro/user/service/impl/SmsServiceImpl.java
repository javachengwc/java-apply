package com.micro.user.service.impl;

import com.micro.user.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    //验证验证码
    public boolean checkCode(String mobile,String code) {
         return true;
    }
}
