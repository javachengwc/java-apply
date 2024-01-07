package com.otd.boot.demo.controller;

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
 * demo接口
 */
@RestController
@RequestMapping("/demo")
@Api(value = "demo接口", description = "demo接口")
@Slf4j
public class DemoController {

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
