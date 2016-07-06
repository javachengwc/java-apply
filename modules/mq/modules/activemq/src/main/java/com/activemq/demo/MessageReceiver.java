package com.activemq.demo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息接收者
 */
public class MessageReceiver {

    // tcp 地址
    public static final String BROKER_URL = "tcp://localhost:61616";

    // 目标，在ActiveMQ管理员控制台创建 http://localhost:8161/admin/queues.jsp
    public static final String DESTINATION = "hoo.mq.queue";

    public static void run() throws Exception {

        Connection connection = null;
        Session session = null;

        try {

            // 创建链接工厂
            ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, BROKER_URL);

            // 通过工厂创建一个连接
            connection = factory.createConnection();

            // 启动连接
            connection.start();

            // 创建一个session会话
            /**
             * 通过connection创建QueueSession对象；
             * 其中第一个参数为是否支持事务，TRUE为支持，false为不支持；
             * 若设为true,则需要手动COMMIT;
             * 第二个参数为响应的模式，一般情况下就设为QueueSession.AUTO_ACKNOWLEDGE
             * */
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            // 创建一个消息队列
            Destination destination = session.createQueue(DESTINATION);

            // 创建消息制作者
            MessageConsumer consumer = session.createConsumer(destination);

            while (true) {

                // 接收数据的时间（等待） 100 s
                Message message = consumer.receive(1000 * 100);
                if(message==null)
                {
                    break;
                }
                System.out.println(message);

                // 提交会话
                session.commit();
//                TextMessage text = (TextMessage) message;
//                if (text != null) {
//                    System.out.println("接收：" + text.getText());
//                } else {
//                    break;
//                }
            }

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

        MessageReceiver.run();

    }

}
