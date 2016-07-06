package com.activemq.demo;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息接收者
 * topic消费者一定好先订阅主题，才能收到发送者的消息
 * 且只能接收它创建以后发送端发送的信息
 *
 * 在运行demo topic程序时，先运行TopicReceiver,再运行TopicSender
 */
public class TopicReceiver {

    // tcp 地址
    public static final String BROKER_URL = "tcp://localhost:61616";

    public static final String TARGET = "hoo.mq.topic";

    public static void run() throws Exception {

        TopicConnection connection = null;
        TopicSession session = null;

        try {

            // 创建链接工厂
            TopicConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);

            // 通过工厂创建一个连接
            connection = factory.createTopicConnection();

            // 启动连接
            connection.start();

            // 创建一个session会话
            session = connection.createTopicSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            // 创建一个消息队列
            Topic topic = session.createTopic(TARGET);

            // 创建消息制作者
            TopicSubscriber subscriber = session.createSubscriber(topic);

            //异步接收消息
            subscriber.setMessageListener(new MessageListener() {

                public void onMessage(Message msg) {
                    if (msg != null) {
                        MapMessage map = (MapMessage) msg;
                        try {
                            System.out.println(map.getLong("time") + "接收#" + map.getString("text"));
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });

            // 休眠10s再关闭
            Thread.sleep(1000 * 10);

        } catch (Exception e) {
            throw e;
        } finally {

            // 关闭释放资源
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {

        TopicReceiver.run();

    }

}