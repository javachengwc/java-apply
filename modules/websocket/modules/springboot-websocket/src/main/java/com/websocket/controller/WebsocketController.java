package com.websocket.controller;

import com.model.base.Resp;
import com.websocket.service.WebsocketSessionFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/websocket")
@Api(value="websocket接口",description="websocket接口")
public class WebsocketController {

  @GetMapping("/queryOnlineCount")
  @ApiOperation("查询在线人数")
  public Resp<Long> queryOnlineCount(){
    Long count =WebsocketSessionFactory.getOnlineCount();
    return  Resp.data(count);
  }

}
