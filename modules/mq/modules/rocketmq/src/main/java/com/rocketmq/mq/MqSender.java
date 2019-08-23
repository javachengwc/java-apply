package com.rocketmq.mq;

import com.rocketmq.config.MqConfig;
import com.rocketmq.model.req.MessageReq;
import com.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class MqSender {

    @Resource(name = "defaultProducer")
    public MQProducer defaultProducer;

    //推送消息
    public boolean pushMsg(MessageReq msgReq) {
        try {
            int type = msgReq.getType()==null?0:msgReq.getType();
            log.info("MqSender pushMsg start,message={},type={}",msgReq.getContent(),type);
            Message message = new Message("DEFAULT_TOPIC", MqConfig.DEFAULT_TAG, JsonUtil.obj2Json(msgReq).getBytes());
            String key = type==1?MqConfig.DEFAULT_KEY1:MqConfig.DEFAULT_KEY0;
            message.setKeys(key);
            //message.setStartDeliverTime(sendTime);
            defaultProducer.send(message);
            return true;
        } catch (Exception e) {
            log.info("MqSender pushMsg error,", e);
            return false;
        }
    }
}
