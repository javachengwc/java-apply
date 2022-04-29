package com.commonservice.invoke.controller.monitor;

import com.commonservice.invoke.model.dto.Server;
import com.model.base.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "服务器监控")
@RestController
@RequestMapping("/monitor")
public class ServerController {

    @GetMapping("/server")
    @ApiOperation("查看服务器信息接口")
    public Resp<Server> getInfo() {
        Server server = new Server();
        try {
            server.copyTo();
        }catch (Exception e) {
            Resp.error();
        }
        return Resp.data(server);
    }
}
