package com.email.client;

import com.caucho.hessian.client.HessianProxyFactory;
import com.email.service.EmailService;

import java.util.HashMap;
import java.util.Map;

public class HessianClient {

    public static void main(String[] args) throws Exception {
        HessianProxyFactory factory = new HessianProxyFactory();
        EmailService emailService = (EmailService) factory.create(EmailService.class, "http://127.0.0.1:10001/mail");

        System.out.println(emailService.ping());
        Map<String, String> mailInfo =new HashMap<String,String>();
        mailInfo.put("message","测试邮件");
        mailInfo.put("subject","测试");
        mailInfo.put("to","cc@cc.com");
        mailInfo.put("toName","cc");

        emailService.send(mailInfo);

    }
}