package com.activemq.demo;

import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

/**
 * Spring JMSTemplate 消息接收者
 * Spring JMS传递消息有两种方式：JMS template和message listener container，前者用于同步收发消息，后者用于异步收发消息
 * Spring的JmsTemplate在高并发、高性能环境中,是不适用的
 * 1、JmsTemplate采用的是”短连接“。JmsTemplate在消费/生产一条消息时，均创建一个全新Consumer/Producer，工作完毕即可关闭，此工作方式的好处是不产生死连接，
 * 但是同时带来了性能的大幅下降，Consumer/Producer的创建，是一个非常耗时的过程，需要连接JMS中间件，注册监听器，确认，关闭过程需要通知中间件卸载监听器。
 * 2、频繁的”短连接“将对JMS中间件稳定性的影响。很多JMS中间件经受不住高并发”短连接“操作，会造成队列假死，消息丢失、消息脏读等问题，特别是在进行select操作过程中更为明显。
 * 3、JmsTemplate限制了对特定JMS中间件的调整。虽然jms和jdbc一样，由商家提供具体的实现，但是现在的商家远远没有像对待jdbc一样对待jms。
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