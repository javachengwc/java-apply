package com.otd.boot.demo.redis.controller;

import com.model.base.Req;
import com.model.base.Resp;
import com.otd.boot.demo.redis.service.RedisManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo redis接口
 */
@RestController
@RequestMapping("/demo/redis")
@Api(value = "demo redis接口", description = "demo redis接口")
@Slf4j
public class RedisController {

  @Autowired
  private RedisManager redisManager;

  @GetMapping("/getDemo")
  @ApiOperation("查询demo")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query")
  })
  public Resp<String> getDemo(@RequestParam(value = "id", required = true) Integer id) {
    redisManager.getRedisTemplate().delete("cc");
    Object aa = redisManager.getRedisTemplate().opsForValue().get("cc");
    log.info("RedisController getDemo aa="+aa);
    return Resp.success("" + id);
  }

  @PostMapping("/updateDemo")
  @ApiOperation("更新demo")
  public Resp<Void> updateDemo(@RequestBody Req<Integer> req) {
    Integer id = req.getData();
    log.info("DemoController updateDemo start，id={}",id);
    return Resp.success();
  }
}
