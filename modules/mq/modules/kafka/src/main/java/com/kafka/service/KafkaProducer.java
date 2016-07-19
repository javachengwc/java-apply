package com.kafka.service;

import com.util.PropertiesLoader;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.*;

/**
 * 消息生成者
 */
public class KafkaProducer {

    private Properties props=null;

    private Producer<String, String> producer = null;

    private ProducerConfig config;

    public KafkaProducer() {
        props = PropertiesLoader.load("producer.properties");
        //获取参数:metadata.broker.list,producer.type,compression.codec,serializer.class,request.required.acks
        //message.send.max.retries,retry.backoff.ms,batch.num.messages
        config = new ProducerConfig(props);
        producer = new Producer<String, String>(config);
    }

    /**
     * 向队列中放入一条消息
     */
    public boolean send(String topicName, String message) {
        if (topicName == null || message == null) {
            return false;
        }
        KeyedMessage<String, String> km = new KeyedMessage<String, String>(topicName, message);
        producer.send(km);
        return true;
    }

    /**
     * 向队列中放入多条消息
     */
    public boolean send(String topicName, Collection<String> messages) {
        if (topicName == null || messages == null) {
            return false;
        }
        if (messages.isEmpty()) {
            return false;
        }
        List<KeyedMessage<String, String>> list = new ArrayList<KeyedMessage<String, String>>();
        int i = 0;
        for (String entry : messages) {
            KeyedMessage<String, String> km = new KeyedMessage<String, String>(topicName, topicName, entry);
            list.add(km);
            i++;
            if (i % 50 == 0) {
                producer.send(list);
                list.clear();
            }
        }

        if (!list.isEmpty()) {
            producer.send(list);
            list.clear();
        }
        return true;
    }

    public void close() {
        producer.close();
    }
}