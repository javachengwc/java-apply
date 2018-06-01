package com.commonservice.sms.service;

import com.commonservice.sms.model.base.Resp;
import com.commonservice.sms.model.vo.SmsVo;

import java.util.Map;

public interface SmsService {

    //发送短信验证码
    public Resp<SmsVo> sendCaptcha(String mobile);

    //验证短信验证码
    public Boolean verifyCaptcha(String mobile,String captcha);

    //发送短信信息
    public Resp<SmsVo> sendMsg(String mobile,String template,Map<String, String> data) ;
}
