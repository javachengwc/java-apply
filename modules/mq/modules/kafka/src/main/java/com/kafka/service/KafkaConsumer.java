package com.kafka.service;

import com.util.PropertiesLoader;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息消费者
 */
public class KafkaConsumer {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private String zookeeperConnect;

    private ConsumerConfig config;

    private String topic;

    private ConsumerConnector connector;

    private int partitionsNum=1;

    private ExecutorService messageReceiveThreadPool;

    //private ExecutorService messageHandleThreadPool;

    public void setPartitionsNum(int partitionsNum) {
        this.partitionsNum = partitionsNum;
    }

    public void init(int maxThreads) throws Exception {
        //获取配置参数zookeeper.connect,zookeeper.connection.timeout.ms,group.id,auto.commit.enable,auto.commit.interval.ms,auto.commit.interval.ms
        Properties properties = PropertiesLoader.load("consumer.properties");
        zookeeperConnect=properties.getProperty("zookeeper.connect");
        config = new ConsumerConfig(properties);
        topic = properties.getProperty("topic.app");
        connector = Consumer.createJavaConsumerConnector(config);

        Map<String, Integer> topics = new HashMap<String, Integer>();
        topics.put(topic, partitionsNum);

        Map<String, List<KafkaStream<byte[], byte[]>>> streams = connector.createMessageStreams(topics);
        List<KafkaStream<byte[], byte[]>> partitions = streams.get(topic);

        messageReceiveThreadPool = Executors.newFixedThreadPool(partitionsNum);
        //messageHandleThreadPool = Executors.newFixedThreadPool(partitionsNum);

        for (KafkaStream<byte[], byte[]> partition : partitions) {
            messageReceiveThreadPool.execute(new MessageRunner(partition));
        }
    }

    public void close() {
        try {
            messageReceiveThreadPool.shutdownNow();
            //messageHandleThreadPool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.shutdown();
        }
    }

    //获取kafka内容任务类
    private class MessageRunner implements Runnable {

        private KafkaStream<byte[], byte[]> partition;

        MessageRunner(KafkaStream<byte[], byte[]> partition) {
            this.partition = partition;
        }

        public void run() {
            while (true) {
                try {
                    for (MessageAndMetadata<byte[], byte[]> item : partition) {
                        String message = new String(item.message());
                        String key = (item.key() == null)?"": new String(item.key());

                        System.out.println("MessageRunner key:"+key+",msg:"+message+
                                ",partition="+item.partition()+",offset="+item.offset()+","+Thread.currentThread().getName() );

                        //....业务操作
                    }
                } catch (Exception e) {
                    logger.error("MessageRunner run error,",e);
                }
            }
        }
    }

}