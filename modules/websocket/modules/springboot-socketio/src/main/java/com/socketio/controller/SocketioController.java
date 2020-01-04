package com.socketio.controller;

import com.model.base.Resp;

import com.socketio.core.SocketioClientFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/socketio")
@Api(value="socketio接口",description="socketio接口")
public class SocketioController {

  @GetMapping("/queryConnectCount")
  @ApiOperation("查询连接数量")
  public Resp<Long> queryConnectCount(){
    Long count = SocketioClientFactory.getConnectCount();
    return  Resp.data(count);
  }

}
