package com.captcha.service;

import com.captcha.util.CaptchaUtil;
import com.captcha.util.MemcachedUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.captcha.dto.Captcha;

import java.util.HashMap;
import java.util.Map;

@Service
public class CaptchaService {
	
	private static Logger logger = Logger.getLogger(CaptchaService.class);

	private  String captchaStr= "0123456789abc";//随机产生的字符串;
    
	/**
     * 产生验证码
     */
    public Captcha genarateCaptcha(String sessionId) {
    	
    	String ca =captchaStr;
    	Captcha captcha =CaptchaHelper.render(ca);
    	String key = sessionId + CaptchaUtil.CAPTCHA_KEY;
    	boolean rt =MemcachedUtil.put(key, captcha.getCode(), 300);//5分钟
        System.out.println("rt="+rt);
		return  captcha;
    }
    
    /**
     * 验证验证码
     */
    public Object checkCaptcha(String sessionId,String captcha,Integer no_clear) {
    	
    	String key = sessionId + CaptchaUtil.CAPTCHA_KEY;
		String code =MemcachedUtil.get(key);
        System.out.println("code:"+code);
        Map<String,Object> result = new HashMap<String,Object>();

		if(!StringUtils.isBlank(code) && code.equalsIgnoreCase(captcha))
		{
			if(no_clear==null ||  1!=no_clear)
			{
                MemcachedUtil.remove(key);
			}
            result.put("error",0);
            result.put("msg","验证通过");

		}else
		{
            result.put("error",1);
            result.put("msg","验证不通过");
		}
        return result;
    }
}
