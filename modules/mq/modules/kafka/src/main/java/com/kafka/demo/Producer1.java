package com.kafka.demo;

import com.util.ThreadUtil;
import kafka.producer.KeyedMessage;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 生产者1
 */
public class Producer1 {

    public static void main(String[] args) {

        //metadata.broker.list=127.0.0.1:9092,xxo10:9092
        //partitioner.class=com....Partitioner
        //producer.type                异步可以提高发送吞吐量，但是也可能导致丢失未发送过去的消息
        //queue.buffering.max.ms       如果是异步，指定每次发送最大间隔时间
        //queue.buffering.max.messages 如果是异步，指定每次发送缓存最大数据量
        //producer.type=sync
        //compression.codec=none
        //serializer.class=kafka.serializer.StringEncoder

        Properties props = new Properties();
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "127.0.0.1:9092");
        //如果broker是多分区的话，制定分区规则
        props.put("partitioner.class","com.kafka.demo.DemoPartitioner");

        //0（默认）表示生产者不用等待broker返回ack
        //1表示当有复本（该复本节点不一定是同步）收到了消息后发回ack给生产者（如果leader挂掉且刚好收到消息的复本也挂掉则消息丢失）。
        // -1表示所有已同步的复本收到了消息后发回ack给生产者（可以保证只要有一个已同步的复本存活就不会有数据丢失)
        //props.put("request.required.acks", "1");

        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);
        for (int i = 0; i < 10; i++) {
            KeyedMessage<String,String> msg = new KeyedMessage<String, String>("tt", "tt" + i);
            producer.send(msg);
        }

        String topic="hello";

        KeyedMessage<String, String> message1=new KeyedMessage<String, String>(topic, "h1", "v1");
        KeyedMessage<String, String> message2=new KeyedMessage<String, String>(topic, "h2", "v2");
        KeyedMessage<String, String> message3=new KeyedMessage<String, String>(topic, "h3", "v3");
        producer.send(message1);

        List<KeyedMessage<String, String>> list = new ArrayList<KeyedMessage<String, String>>() ;
        list.add( message2 );
        list.add(message3);
        producer.send( list );

        for (int i = 0; i < 1000; i++) {
            KeyedMessage<String,String> msg = new KeyedMessage<String, String>(topic, "tt" + i);
            producer.send(msg);
        }

        //4分区的一个topic
        //kafka-topics.sh --create --zookeeper 127.0.0.1:2181 --replication-factor 1 --partitions 4 --topic mult
        String multTopic="mult";

        for (int i = 0; i < 1000; i++) {
            KeyedMessage<String,String> msg = new KeyedMessage<String, String>(multTopic, ""+i,"mult" + i);
            producer.send(msg);
        }

        ThreadUtil.sleep(2*1000l);
        //关闭
        producer.close();
    }
}
