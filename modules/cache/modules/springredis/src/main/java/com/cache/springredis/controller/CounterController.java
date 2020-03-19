package com.cache.springredis.controller;

import com.cache.springredis.service.CounterService;
import com.model.base.Req;
import com.model.base.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counter")
@Api(value="计数器接口",description="计数器接口")
public class CounterController {

    @Autowired
    private CounterService counterService;

    @GetMapping("/getCounterValue")
    @ApiOperation("查询计数器值")
    public Resp<Integer> getCounterValue(){
       Integer count = counterService.getCounterValue();
       return Resp.data(count);
    }

    @PostMapping("/changeCount")
    @ApiOperation("改变值")
    public Resp<Long> changeCount(@RequestBody Req<Long> req){
        Long count = req.getData();
        Long after = counterService.changeCount(count);
        return Resp.data(after);
    }
}
