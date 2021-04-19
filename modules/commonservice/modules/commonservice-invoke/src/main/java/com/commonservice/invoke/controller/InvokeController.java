package com.commonservice.invoke.controller;

import com.commonservice.invoke.model.vo.InvokeVo;
import com.commonservice.invoke.service.InvokeService;
import com.model.base.Req;
import com.model.base.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "调用接口")
@RestController
@RequestMapping("/invoke")
@Slf4j
public class InvokeController {

    @Autowired
    private InvokeService invokeService;

    @RequestMapping(value = "/invoke", method = RequestMethod.POST)
    @ApiOperation(value = "接口调用",notes = "接口调用")
    public Resp<Void> invoke(@RequestBody Req<InvokeVo> req){
        InvokeVo invokeVo= req.getData();
        String link = invokeVo.getResourceLink();
        log.info("InvokeController invoke start,link={}",link);
        Resp<Void>  resp=invokeService.invoke(invokeVo);
        return resp;
    }
}
