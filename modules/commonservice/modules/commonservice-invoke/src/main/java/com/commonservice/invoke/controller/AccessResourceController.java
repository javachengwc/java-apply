package com.commonservice.invoke.controller;

import com.commonservice.invoke.model.entity.AccessResource;
import com.commonservice.invoke.model.param.AccessResourceQuery;
import com.commonservice.invoke.model.vo.AccessResourceVo;
import com.commonservice.invoke.service.AccessResourceService;
import com.model.base.Req;
import com.model.base.Resp;
import com.util.JsonUtil;
import com.util.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "访问资源接口")
@RestController
@RequestMapping("/access/resource")
@Slf4j
public class AccessResourceController {

    @Autowired
    private AccessResourceService accessResourceService;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询访问接口",notes = "分页查询访问接口")
    public Resp<Page<AccessResourceVo>> page(@RequestBody Req<AccessResourceQuery> req){
        AccessResourceQuery query= req.getData();
        log.info("AccessResourceController page start,query={}", JsonUtil.obj2Json(query));
        query.genStart();
        Page<AccessResourceVo> pageData=accessResourceService.page(query);
        return Resp.data(pageData);
    }

    @GetMapping("/getById")
    @ApiOperation("查询访问接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "接口id", required = true, dataType = "Long", paramType = "query")
    })
    public Resp<AccessResource> getById(@RequestParam(value = "id") Long id){
        log.info("AccessResourceController getById start,id={}", id);
        AccessResource data=accessResourceService.getById(id);
        return Resp.data(data);
    }
}
