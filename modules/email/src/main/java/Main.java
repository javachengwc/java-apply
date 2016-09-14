import com.email.Email;
import com.email.EmailSender;

import java.io.File;
import java.util.HashMap;

/**
 * 带附件的邮件发送
 */
public class Main {

    public static void main(String args []) throws Exception
    {

        Email email = new Email();
        email.setFrom("from@163.com");
        email.setTo("cc@163.com");
        email.setUserName("username");
        email.setPassword("password");
        email.setIfAuth(true);
        email.setContent("测试");
        email.setSubject("just a test");
        email.setDisplayName("show name");
        email.setSmtpServer("smtp.163.com");
        //File file = new File("E:\\abc.sql");
        email.addAttachfile("E:\\abc.sql");
        HashMap result =new EmailSender().send(email);

        System.out.println("result :"+result);
    }
}
