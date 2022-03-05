package com.es.consumer.controller;

import com.es.consumer.model.entity.Test;
import com.es.consumer.model.vo.TestVo;
import com.es.consumer.service.TestService;
import com.model.base.Req;
import com.model.base.Resp;
import com.model.base.RespHeader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api("test接口")
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @ApiOperation("根据id查询test数据")
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "query")
    })
    public Resp<TestVo> queryById(@RequestParam(value = "id",required = true) Long id) {
        Resp<TestVo> resp = new Resp<TestVo>();
        Test test  = testService.getById(id);
        if(test==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("无数据");
            return resp;
        }
        TestVo testVo =new TestVo();
        BeanUtils.copyProperties(test, testVo);
        resp.setData(testVo);
        return resp;
    }

    @ApiOperation("增加test数据")
    @RequestMapping(value = "/addTest", method = RequestMethod.POST)
    public Resp<TestVo> addTest(@RequestBody Req<TestVo> req) {
        Resp<TestVo> resp = new Resp<TestVo>();
        TestVo testVo = req.getData();
        if(StringUtils.isBlank(testVo.getName())) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        Date now = new Date();
        Test test = new Test();
        BeanUtils.copyProperties(testVo, test);
        test.setCreateTime(now);
        test.setModifyTime(now);
        testService.save(test);
        BeanUtils.copyProperties(test, testVo);
        resp.setData(testVo);
        return resp;
    }
}
