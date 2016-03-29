package com.email;
import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {

    /**
     * 发送邮件
     */
    @SuppressWarnings("unchecked")
    public HashMap send(Email email){
        HashMap map=new HashMap();
        map.put("state", "success");
        String message="邮件发送成功！";
        Session session=null;
        Properties props = System.getProperties();
        props.put("mail.smtp.host", email.getSmtpServer());
        if(email.isIfAuth()){ //服务器需要身份认证
            props.put("mail.smtp.auth","true");
            SmtpAuth smtpAuth=new SmtpAuth(email.getUsername(),email.getPassword());
            session=Session.getDefaultInstance(props, smtpAuth);
        }else{
            props.put("mail.smtp.auth","false");
            session=Session.getDefaultInstance(props, null);
        };
        session.setDebug(false);
        Transport trans = null;
        try {
            Message msg = new MimeMessage(session);
            try{
                Address from_address = new InternetAddress(email.getFrom(), email.getDisplayName());
                msg.setFrom(from_address);
            }catch(java.io.UnsupportedEncodingException e){
                e.printStackTrace();
            }
            String [] s = email.getTo().split(",");
            System.out.println(s.length);
            InternetAddress[] address=new InternetAddress[s.length];
            for(int i=0; i < s.length; i++) {
                System.out.println(s[i]);
                address[i] = new InternetAddress(s[i]);
            }

            msg.setRecipients(Message.RecipientType.TO,address);
            msg.setSubject(email.getSubject());
            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(email.getContent().toString(), "text/html;charset=gb2312");
            mp.addBodyPart(mbp);
            Vector file =email.getFile();
            if(!file.isEmpty()){//有附件
                Enumeration efile=file.elements();
                while(efile.hasMoreElements()){
                    mbp=new MimeBodyPart();
                    String filename=efile.nextElement().toString(); //选择出每一个附件名
                    FileDataSource fds=new FileDataSource(filename); //得到数据源
                    mbp.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart
                    mbp.setFileName(fds.getName());  //得到文件名同样至入BodyPart
                    mp.addBodyPart(mbp);
                }
                file.removeAllElements();
            }
            msg.setContent(mp); //Multipart加入到信件
            msg.setSentDate(new Date());     //设置信件头的发送日期
            //发送信件
            msg.saveChanges();
            trans = session.getTransport("smtp");
            trans.connect(email.getSmtpServer(), email.getUsername(), email.getPassword());
            trans.sendMessage(msg, msg.getAllRecipients());
            trans.close();

        }catch(AuthenticationFailedException e){
            map.put("state", "failed");
            message="邮件发送失败！错误原因：\n"+"身份验证错误!";
            e.printStackTrace();
        }catch (MessagingException e) {
            message="邮件发送失败！错误原因：\n"+e.getMessage();
            map.put("state", "failed");
            e.printStackTrace();
            Exception ex = null;
            if ((ex = e.getNextException()) != null) {
                System.out.println(ex.toString());
                ex.printStackTrace();
            }
        }
        //System.out.println("\n提示信息:"+message);
        map.put("message", message);
        return map;
    }
}
