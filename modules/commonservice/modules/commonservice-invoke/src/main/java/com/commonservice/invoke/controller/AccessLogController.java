package com.commonservice.invoke.controller;

import com.commonservice.invoke.model.entity.AccessLog;
import com.commonservice.invoke.model.param.AccessLogQuery;
import com.commonservice.invoke.model.vo.AccessLogVo;
import com.commonservice.invoke.service.AccessLogService;
import com.model.base.PageVo;
import com.model.base.Req;
import com.model.base.Resp;
import com.util.JsonUtil;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "调用日志接口")
@RestController
@RequestMapping("/accesslog")
@Slf4j
public class AccessLogController {

    @Autowired
    private AccessLogService accessLogService;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询接口日志",notes = "分页查询接口日志")
    public Resp<PageVo<AccessLogVo>> page(@RequestBody Req<AccessLogQuery> req){
        AccessLogQuery query= req.getData();
        log.info("AccessLogController page start,query={}", JsonUtil.obj2Json(query));
        query.genPage();
        PageVo<AccessLog> pageData=accessLogService.page(query);
        PageVo<AccessLogVo> rtPage = TransUtil.transEntityWithJson(pageData,PageVo.class);
        return Resp.data(rtPage);
    }
}
