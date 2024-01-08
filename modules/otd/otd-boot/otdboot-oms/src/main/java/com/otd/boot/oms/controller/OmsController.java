package com.otd.boot.oms.controller;

import com.otd.boot.web.base.model.vo.Req;
import com.otd.boot.web.base.model.vo.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * oms接口
 */
@RestController
@RequestMapping("/oms")
@Api(value = "oms接口", description = "oms接口")
@Slf4j
public class OmsController {

  @GetMapping("/getDemo")
  @ApiOperation("查询demo")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query")
  })
  public Resp<String> getDemo(@RequestParam(value = "id", required = true) Integer id) {
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
