package com.micro.user.controller;

import com.micro.user.service.SmsService;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.util.regex.RegexUtil;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@Api(value="短信接口",description="短信接口")
@RequestMapping("/sms")
@RestController
public class SmsController {

    private static Logger logger= LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private SmsService smsService;

    @PostMapping("sendCaptcha")
    public Resp<Void> sendCaptcha(@RequestBody Req<String> req) {
        logger.info("SmsController sendCaptcha start,req={}",req);
        Resp<Void> resp = new Resp<Void>();
        String mobile =req.getData();
        if (StringUtils.isEmpty(mobile) || !RegexUtil.isCellPhone(mobile)) {
            resp=Resp.error("请输入正确手机号");
            return resp;
        }
        //smsServie.sendCaptcha(mobile);
        return resp;
    }
}
