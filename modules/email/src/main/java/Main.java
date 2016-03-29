import com.email.Email;
import com.email.EmailSender;

import java.util.HashMap;

/**
 * 发送邮件
 */
public class Main {

    public static void main(String args [])
    {

        Email email = new Email();
        email.setFrom("xx@163.com");
        email.setTo("xx@163.com");
        email.setUserName("xx");
        email.setPassword("xx");
        email.setIfAuth(true);
        email.setContent("just a test");
        email.setSubject("just a test");
        email.setDisplayName("show name");
        email.setSmtpServer("smtp.163.com");
        HashMap result =new EmailSender().send(email);

        System.out.println("result :"+result);
    }
}
