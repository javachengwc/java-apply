package com.commonservice.sms.service.manager;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.commonservice.sms.config.SmsConfig;
import com.commonservice.sms.constant.Constant;
import com.commonservice.sms.enums.SmsTemplateEnum;
import com.commonservice.sms.model.SmsAliSendResult;
import com.commonservice.sms.model.pojo.SmsTemplate;
import com.commonservice.sms.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SmsAliManager {

    private static Logger logger = LoggerFactory.getLogger(SmsAliManager.class);

    private static SmsAliManager inst = new SmsAliManager();

    public static SmsAliManager getInstance() {
        return inst;
    }

    private SmsConfig smsConfig;

    private  SmsAliManager() {
        init();
    }

    public void init() {
        logger.info("SmsAliManager init start ................");
        smsConfig=SpringContextUtil.getBean(SmsConfig.class);
        int maxSendCount = smsConfig.getMaxSendCount();
        String product = smsConfig.getAli().getProduct();
        logger.info("SmsAliManager smsConfig maxSendCount={},product={}",maxSendCount,product);

        logger.info("SmsAliManager init end ................");
    }

    public SmsAliSendResult sendCaptcha(String mobile, String captcha) {
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("captcha", captcha);
        SmsAliSendResult result =send(mobile, SmsTemplateEnum.CAPTCHA.getCode(), params);
        return result;
    }

    public SmsAliSendResult send(String mobile, String template, Map<String, String> params) {
        SmsAliSendResult result = new SmsAliSendResult();
        IClientProfile profile = DefaultProfile.getProfile(Constant.REGION_ID, smsConfig.getAli().getAccessId(), smsConfig.getAli().getAccessKey());
        try {
            DefaultProfile.addEndpoint(Constant.ENDPOINT_NAME, Constant.REGION_ID, smsConfig.getAli().getProduct(), smsConfig.getAli().getDomain());
            IAcsClient acsClient = new DefaultAcsClient(profile);

            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(mobile);//手机号
            request.setSignName(Constant.SIGN_NAME); //签名
            request.setTemplateCode(template);//模板
            //模板中的变量替换JSON串,如模板内容为"验证码为${code}",此处的值为{"code":"xxxxxx"}
            if (params != null && !params.isEmpty()) {
                request.setTemplateParam(JSON.toJSONString(params));
            }
            SendSmsResponse response = acsClient.getAcsResponse(request);
            String code =response.getCode();
            String message = response.getMessage();
            result.setBizId(response.getBizId());
            result.setCode(code);
            result.setMessage(message);
            result.setRequestId(response.getRequestId());
            boolean success = result.isSuccess();
            if(!success) {
                logger.warn("SmsAliManager send fail,mobile={},code={},message={}",mobile,code,message);
            }
            return result;
        } catch (ClientException e) {
            logger.error("SmsAliManager send fail,mobile={}",mobile,e);
            result.setMessage("服务异常");
        }
        return  result;
    }

}
