package com.activemq.demo;

import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 * Spring JMSTemplate 消息接收者
 */
public class ReceiverWithSpring {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        JmsTemplate jmsTemplate = (JmsTemplate) ctx.getBean("jmsTemplate");
        while(true) {
            //如果队列没消息，将会阻塞在此，最好设置个超时
            Map<String, Object> map =  (Map<String, Object>) jmsTemplate.receiveAndConvert();
            System.out.println("收到消息：" + map.get("message"));
        }
    }
}