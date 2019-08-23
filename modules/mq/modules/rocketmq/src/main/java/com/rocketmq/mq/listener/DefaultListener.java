package com.rocketmq.mq.listener;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.rocketmq.config.MqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class DefaultListener implements MessageListenerConcurrently {

    private static Logger logger = LoggerFactory.getLogger(DefaultListener.class);

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if(CollectionUtils.isEmpty(msgs)){
            logger.info("DefaultListener consumeMessage 接收的消息为空");
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MessageExt messageExt = msgs.get(0);
        String key = messageExt.getKeys();
        logger.info("DefaultListener consumeMessage 接收到消息,key={}",key);
        switch (key) {
            case MqConfig.DEFAULT_KEY0: {
                String msg = new String(messageExt.getBody());
                logger.info("DefaultListener consumeMessage 接收到消息0,msg={},key={}",msg,key);
                break;
            }
            case MqConfig.DEFAULT_KEY1: {
                String msg = new String(messageExt.getBody());
                logger.info("DefaultListener consumeMessage 接收到消息1,msg={},key={}",msg,key);
                break;
            }
            default:
                logger.info("DefaultListener consumeMessage 无对应处理,key={}", key);
                break;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
