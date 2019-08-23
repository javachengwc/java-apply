package com.rocketmq.service.impl;

import com.rocketmq.model.req.MessageReq;
import com.rocketmq.mq.MqSender;
import com.rocketmq.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MqSender mqSender;

    public boolean pushMsg(MessageReq msgReq) {
        log.info("MessageServiceImpl pushMsg start ,content={},type={}",msgReq.getContent(),msgReq.getType());
        mqSender.pushMsg(msgReq);
        return true;
    }
}
