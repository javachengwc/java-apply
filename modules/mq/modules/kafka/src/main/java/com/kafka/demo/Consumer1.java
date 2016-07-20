package com.kafka.demo;

import com.util.PropertiesLoader;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 消费者1
 * 先启动消费者程序，再启动生产者程序
 * kafka的consumer接口，有两种版本
 *     high-level版本,此程序的consumer就是high-level
 *     low-level版本,就是SimpleConsumer
 */
public class Consumer1 {

    public static void main(String[] args) throws Exception {

        Properties props = PropertiesLoader.load("consumer.properties");
        //指定kafka等待多久zookeeper回复（ms）以便放弃并继续消费。
        props.put("zookeeper.session.timeout.ms", "4000");
        //指定zookeeper同步最长延迟多久再产生异常
        props.put("zookeeper.sync.time.ms", "2000");
        // 如果为真，consumer所fetch的消息的offset将会自动的同步到zookeeper。
        props.put("auto.commit.enable", "true");
        //指定多久消费者更新offset到zookeeper中。默认是1分钟，太长了，此处设定为1秒钟
        //注意offset更新时基于time而不是每次获得的消息。一旦在更新zookeeper发生异常并重启，将可能拿到已拿到过的消息
        props.put("auto.commit.interval.ms", "1000");
        ConsumerConnector connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

        HashMap<String, Integer> topicConnect = new HashMap<String, Integer>();
        String topic = "hello";
        topicConnect.put(topic,1); //设置topic和消费者数量

        Map<String, List<KafkaStream<byte[], byte[]>>> streams= connector.createMessageStreams(topicConnect);

        List<KafkaStream<byte[], byte[]>> list = streams.get(topic);
        int cnt = (list==null)?0:list.size();
        System.out.println("Consumer1 list size="+cnt);
        for (KafkaStream<byte[], byte[]> stream : list) {
            new Thread(new Workers( stream.iterator() )).start();
        }
    }

    static class Workers implements Runnable{

        private ConsumerIterator<byte[], byte[]> iterator ;

        public Workers(ConsumerIterator<byte[], byte[]> iterator) {
            this.iterator = iterator;
        }

        public void run() {
            System.out.println("Workers run start,thread-->"+Thread.currentThread().getName());
            //线程一直不会退出，因为如果莫有消息，iterator.hasNext()会阻塞
            while ( iterator.hasNext() ){
                MessageAndMetadata<byte[], byte[]> next = iterator.next();
                String key = (next.key() == null)?"": new String(next.key());
                String message = (next.message() == null)?"": new String(next.message());

                System.out.println("Workers key:"+key+",msg:"+message+
                        ",partition="+next.partition()+",offset="+next.offset()+","+Thread.currentThread().getName() );

            }
        }
    }

}
