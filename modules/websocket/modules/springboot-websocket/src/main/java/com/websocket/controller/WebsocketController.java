package com.websocket.controller;

import com.model.base.Resp;
import com.websocket.service.WebsocketSession;
import com.websocket.service.WebsocketSessionFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;

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

  @GetMapping("/doShell")
  @ApiOperation("执行shell")
  public Resp<Void> doShell(){

    ProcessExecutor exec=new ProcessExecutor().command("ping", "www.baidu.com","-t").timeout(30, TimeUnit.SECONDS);
    try {
      exec.redirectOutput(new LogOutputStream() {
        @Override
        protected void processLine(String line) {
          System.out.println("----------------" + line);
          for(WebsocketSession per: WebsocketSessionFactory.getSessionCollection()){
            try {
              per.sendMessage(line);
            } catch (IOException e) {
              e.printStackTrace(System.out);
              continue;
            }
          }
        }
      }).execute();
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
    return Resp.success();
  }
}
