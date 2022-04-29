package com.commonservice.invoke.controller;

import com.commonservice.invoke.model.entity.ResourceInvoke;
import com.commonservice.invoke.model.param.ResourceInvokeQuery;
import com.commonservice.invoke.model.vo.InvokeVo;
import com.commonservice.invoke.model.vo.ResourceInvokeVo;
import com.commonservice.invoke.service.ResourceInvokeService;
import com.model.base.PageVo;
import com.model.base.Req;
import com.model.base.Resp;
import com.util.JsonUtil;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "调用接口")
@RestController
@RequestMapping("/resource/invoke")
@Slf4j
public class ResourceInvokeController {

    @Autowired
    private ResourceInvokeService resourceInvokeService;

    @RequestMapping(value = "/invoke", method = RequestMethod.POST)
    @ApiOperation(value = "接口调用",notes = "接口调用")
    public Resp<Object> invoke(@RequestBody Req<InvokeVo> req){
        InvokeVo invokeVo= req.getData();
        log.info("ResourceInvokeController invoke start,resourceId={}",invokeVo.getResourceId());
        Resp<Object>  resp=resourceInvokeService.invoke(invokeVo);
        return resp;
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询接口调用记录",notes = "分页查询接口调用记录")
    public Resp<PageVo<ResourceInvokeVo>> page(@RequestBody Req<ResourceInvokeQuery> req){
        ResourceInvokeQuery query= req.getData();
        log.info("ResourceInvokeController page start,query={}", JsonUtil.obj2Json(query));
        query.genPage();
        PageVo<ResourceInvoke> pageData=resourceInvokeService.page(query);
        PageVo<ResourceInvokeVo> rtPage = TransUtil.transEntityWithJson(pageData,PageVo.class);
        return Resp.data(rtPage);
    }

    @GetMapping("/getById")
    @ApiOperation("查询接口调用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "接口调用记录id", required = true, dataType = "Long", paramType = "query")
    })
    public Resp<ResourceInvoke> getById(@RequestParam(value = "id") Long id){
        log.info("ResourceInvokeController getById start,id={}", id);
        ResourceInvoke data=resourceInvokeService.getById(id);
        return Resp.data(data);
    }
}
