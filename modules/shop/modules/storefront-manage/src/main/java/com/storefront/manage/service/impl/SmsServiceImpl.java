package com.storefront.manage.service.impl;

import com.storefront.manage.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    public  boolean verifyCaptcha(String mobile, String captcha) {
        return true;
    }
}
