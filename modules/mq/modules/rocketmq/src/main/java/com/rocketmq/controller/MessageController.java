package com.rocketmq.controller;

import com.model.base.Req;
import com.model.base.Resp;
import com.rocketmq.model.req.MessageReq;
import com.rocketmq.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value="消息接口",description="消息接口")
@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation(value = "推送消息", notes = "推送消息")
    @RequestMapping(value = "/pushMsg", method = RequestMethod.POST)
    public Resp<Void> pushMsg(@RequestBody Req<MessageReq> req) {
        MessageReq reqData = req.getData();
        messageService.pushMsg(reqData.getContent(),reqData.getType());
        return Resp.success();
    }
}
