package com.es.consumer.base.consumer;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.es.consumer.base.service.MessageService;
import com.es.consumer.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * kafka消息消费
 */
public class MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    //消费者
    private final ConsumerConnector consumer;

    //信息topic
    private String topic;

    //消费组
    private String group;

    //线程池
    private ExecutorService executor;

    //信息服务
    private MessageService messageService;

    //配置信息
    private ApplicationConfig applicationConfig;

    public MessageConsumer(ApplicationConfig applicationConfig, MessageService messageService, String topic, String group) {
        this.applicationConfig = applicationConfig;
        this.messageService = messageService;
        this.topic = topic;
        this.group = group;
        this.consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
        logger.info("MessageConsumer created, topic={},group={},service={}", topic, group, messageService.getClass().getSimpleName());
    }

    /**
     * 初始化consumer配置
     */
    private ConsumerConfig createConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", applicationConfig.getZkAddress());
        props.put("zookeeper.session.timeout.ms", applicationConfig.getZkSessionTimeout());
        props.put("zookeeper.connection.timeout.ms", applicationConfig.getZkConnectTimeout());
        props.put("zookeeper.sync.time.ms", applicationConfig.getZkSyncTime());
        props.put("rebalance.max.retries", applicationConfig.getRebalanceMaxRetries());
        props.put("rebalance.backoff.ms", applicationConfig.getRebalanceBackoff());
        props.put("group.id", group);
        props.put("auto.offset.reset", "largest"); // smallest
        props.put("auto.commit.interval.ms", "100");
        return new ConsumerConfig(props);
    }

    /**
     * 消费消息
     */
    public void consume(int threadCount) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, Integer.valueOf(threadCount));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);
        executor = Executors.newFixedThreadPool(threadCount);

        for (final KafkaStream<byte[], byte[]> stream : streams) {
            executor.submit(() -> {
                ConsumerIterator<byte[], byte[]> it = stream.iterator();
                while (it.hasNext()) {
                    String message = new String(it.next().message());
                    try {
                        //处理消息
                        messageService.handleMessage(message);
                    } catch (Exception e) {
                        logger.error("MessageConsumer consume error,topic={},group={},messge={}", topic, group, message, e);
                    }
                }
            });
        }
    }

    /**
     * 关闭消费者
     */
    public void shutdown() {
        logger.info("MessageConsumer shutdown start,topic={},group={},service={}", topic, group, messageService.getClass().getSimpleName());
        if (consumer != null) {
            consumer.shutdown();
        }
        if (executor != null) {
            executor.shutdown();
        }
        try {
            while (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                logger.info("MessageConsumer thread doing 执行中,topic={},group={}", topic, group);
            }
        } catch (Exception e) {
            logger.info("MessageConsumer executor awaitTermination error，topic={},group={}", topic, group, e);
        }
        logger.info("MessageConsumer shutdown start,topic={},group={}", topic, group);
    }
}
