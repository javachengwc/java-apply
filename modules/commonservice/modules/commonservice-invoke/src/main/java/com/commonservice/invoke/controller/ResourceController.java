package com.commonservice.invoke.controller;

import com.commonservice.invoke.model.entity.AccessResource;
import com.commonservice.invoke.model.param.ResourceQuery;
import com.commonservice.invoke.service.AccessResourceService;
import com.model.base.Req;
import com.model.base.Resp;
import com.util.JsonUtil;
import com.util.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "访问资源接口")
@RestController
@RequestMapping("/resource")
@Slf4j
public class ResourceController {

    @Autowired
    private AccessResourceService accessResourceService;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询访问资源",notes = "分页查询访问资源")
    public Resp<Page<AccessResource>> page(@RequestBody Req<ResourceQuery> req){
        ResourceQuery query= req.getData();
        log.info("ResourceController page start,query={}", JsonUtil.obj2Json(query));
        Page<AccessResource> pageData=accessResourceService.page(query);
        return Resp.data(pageData);
    }
}
