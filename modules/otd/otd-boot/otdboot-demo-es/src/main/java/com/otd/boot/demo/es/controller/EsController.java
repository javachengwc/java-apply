package com.otd.boot.demo.es.controller;

import com.model.base.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo es接口
 */
@RestController
@RequestMapping("/demo/es")
@Api(value = "demo es接口", description = "demo es接口")
@Slf4j
public class EsController {

  @GetMapping("/getDemo")
  @ApiOperation("查询demo")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query")
  })
  public Resp<String> getDemo(@RequestParam(value = "id", required = true) Integer id) {
    log.info("RedisController getDemo id="+id);
    return Resp.success("" + id);
  }

}
