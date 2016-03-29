package com.captcha.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.captcha.dto.Captcha;
import com.captcha.service.CaptchaService;

@Controller
public class CaptchaController {
	
	private static Logger logger = Logger.getLogger(CaptchaController.class);
	
	@Autowired
	private CaptchaService captchaService;

    @ResponseBody
	@RequestMapping("getCaptcha")
	public void getCaptcha(String sessionId,HttpServletResponse response) {
		Captcha captcha = captchaService.genarateCaptcha(sessionId);
        logger.error("captcha get:"+sessionId+" "+captcha.getCode());
        System.out.println("captcha get:"+sessionId+" "+captcha.getCode());
        output(captcha.getImage(),response);
	}
	
	public void output(BufferedImage image ,HttpServletResponse response)
	{
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		
		ServletOutputStream sos = null;
		try {
			sos = response.getOutputStream();
			ImageIO.write(image, "jpeg", sos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				sos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@ResponseBody
	@RequestMapping("checkCaptcha")
	public Object checkCaptcha(String sessionId,String captcha,Integer no_clear) {


        if(StringUtils.isBlank(sessionId))
        {
            Map<String,Object> result = new HashMap<String,Object>(2);
            result.put("error",1);
            result.put("msg","请不要禁用了浏览器Cookie");
            return result;
        }
		if(StringUtils.isBlank(captcha))
		{
            Map<String,Object> result = new HashMap<String,Object>(2);
            result.put("error",1);
            result.put("msg","效验码不能为空");
            return result;
		}
		logger.error("captcha check:"+sessionId+" "+captcha);
		return captchaService.checkCaptcha(sessionId, captcha, no_clear);
	}
}
