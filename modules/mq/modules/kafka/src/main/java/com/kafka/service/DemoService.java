package com.kafka.service;

import com.kafka.util.JsonHelper;
import kafka.consumer.*;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DemoService {

    private static Logger logger = LoggerFactory.getLogger(DemoService.class);

    private String zooKeeper;

    private String groupId;

    private String topic;

    public void setZooKeeper(String zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void hander(){

        Properties properties = new Properties();
        properties.put("zookeeper.connect",zooKeeper);
        properties.put("group.id", groupId);
        properties.put("auto.offset.reset", "largest");

        ConsumerConfig consumerConfig = new ConsumerConfig(properties);
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

        Whitelist whitelist = new Whitelist(topic);
        List<KafkaStream<byte[], byte[]>> partitions = consumerConnector.createMessageStreamsByFilter(whitelist);
        if (partitions.isEmpty()){
            logger.info("partitions empty!");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.info("线程等待1秒异常",e);
            }
        }

        for (KafkaStream<byte[], byte[]> partition : partitions){

            ConsumerIterator<byte[], byte[]> iterator = partition.iterator();
            while ( iterator.hasNext() ){
                MessageAndMetadata<byte[], byte[]> next = iterator.next();
                try {
                    String jsonStr = new String(next.message(),"utf-8");

                    Map<String,Object> strMap = JsonHelper.json2Map(jsonStr);

                    //具体处理
                    //....................

                } catch (Exception e) {
                    logger.error("kafka内容读取异常", e);
                }
            }
        }
    }

}
