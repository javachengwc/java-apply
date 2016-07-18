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
        //producer.type=sync
        //compression.codec=none
        //serializer.class=kafka.serializer.StringEncoder

        Properties props = new Properties();
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "127.0.0.1:9092");
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

        ThreadUtil.sleep(2*1000l);
        //关闭
        producer.close();
    }
}
