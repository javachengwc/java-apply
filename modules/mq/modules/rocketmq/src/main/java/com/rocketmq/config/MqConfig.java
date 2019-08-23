package com.rocketmq.config;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.MQConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MQProducer;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.rocketmq.mq.listener.DefaultListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MqConfig {

    public static final String DEFAULT_TOPIC = "DEFAULT_TOPIC";

    public static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    public static final String DEFAULT_TAG = "default_tag";

    public static final String DEFAULT_KEY0 = "default_key0";

    public static final String DEFAULT_KEY1 = "default_key1";

    @Value("${rocketmq.namesrvAddr}")
    private String namesrvAddr;

    //消息最大大小，默认4M
    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize ;

    //消息发送超时时间，默认3秒
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;

    //消息发送失败重试次数，默认2次
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;

    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;

    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;

    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;

    //生产者
    @Bean(name = "defaultProducer")
    public MQProducer defaultProducer() {
        DefaultMQProducer producer= new DefaultMQProducer(DEFAULT_GROUP);
        producer.setNamesrvAddr(this.namesrvAddr);
        producer.setCreateTopicKey(DEFAULT_TOPIC); //创建这个topic,一般创建不了，最好在mq服务上预先创建

        if(this.maxMessageSize!=null){
            producer.setMaxMessageSize(this.maxMessageSize);
        }
        if(this.sendMsgTimeout!=null){
            producer.setSendMsgTimeout(this.sendMsgTimeout);
        }
        if(this.retryTimesWhenSendFailed!=null){
            producer.setRetryTimesWhenSendFailed(this.retryTimesWhenSendFailed);
        }

        try {
            producer.start();
            log.info("MqConfig defaultProducer start,namesrvAddr={}",namesrvAddr);
        } catch (MQClientException e) {
            log.error("MqConfig defaultProducer error,",e);
            throw new RuntimeException(e);
        }
        return producer;

    }

    //消费者
    @Bean(name = "defaultConsumer")
    public MQConsumer defaultConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(DEFAULT_GROUP);
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setConsumeThreadMin(consumeThreadMin);
        consumer.setConsumeThreadMax(consumeThreadMax);
        consumer.registerMessageListener(new DefaultListener());

        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费，如果非第一次启动，那么按照上次消费的位置继续消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //设置一次消费消息的条数，默认为1条
        consumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        try {
            //设置消费者订阅的主题和tag，如果是订阅该主题下的所有tag，tag使用*；
            //如果指定订阅该主题下的某些tag，使用||分割，如tag1||tag2||tag3
            consumer.subscribe(DEFAULT_TOPIC, "*");
            consumer.start();
            log.info("MqConfig defaultConsumer start,namesrvAddr={},topic={}",namesrvAddr,DEFAULT_TOPIC);
        } catch (Exception e) {
            log.error("MqConfig defaultConsumer error,",e);
            throw new RuntimeException(e);
        }
        return consumer;
    }
}
