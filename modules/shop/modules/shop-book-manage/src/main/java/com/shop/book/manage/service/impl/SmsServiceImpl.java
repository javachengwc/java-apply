package com.shop.book.manage.service.impl;

import com.shop.book.manage.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    public  boolean verifyCaptcha(String mobile, String captcha) {
        return true;
    }
}
