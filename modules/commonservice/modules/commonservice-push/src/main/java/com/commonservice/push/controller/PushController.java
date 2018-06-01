package com.commonservice.push.controller;

import com.commonservice.push.model.base.Req;
import com.commonservice.push.model.base.Resp;
import com.commonservice.push.model.vo.PushMessageVo;
import com.commonservice.push.service.PushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "推送接口")
@RestController
@RequestMapping("/push")
public class PushController {

    private static Logger logger= LoggerFactory.getLogger(PushController.class);

    @Autowired
    private PushService pushService;

    @RequestMapping(value = "/push", method = RequestMethod.POST)
    @ApiOperation(value = "推送消息",notes = "推送消息")
    public Resp<Void> push(@RequestBody Req<PushMessageVo> req){
        PushMessageVo messageVo= req.getData();
        String deviceToken = messageVo==null?null:messageVo.getDeviceToken();
        String message = messageVo==null?null:messageVo.getMessage();
        logger.info("PushController push start,deviceToken={},message={}",deviceToken,message);
        Resp<Void>  resp=pushService.push(messageVo);
        return resp;
    }
}
