package com.commonservice.invoke.controller;

import com.commonservice.invoke.model.entity.ResourceInvoke;
import com.commonservice.invoke.model.param.ResourceInvokeQuery;
import com.commonservice.invoke.model.vo.InvokeVo;
import com.commonservice.invoke.model.vo.ResourceInvokeVo;
import com.commonservice.invoke.service.ResourceInvokeService;
import com.commonservice.invoke.util.HttpResponse;
import com.model.base.PageVo;
import com.model.base.Req;
import com.model.base.Resp;
import com.util.JsonUtil;
import com.util.TransUtil;
import com.util.base.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Api(description = "调用接口")
@RestController
@RequestMapping("/resource/invoke")
@Slf4j
public class ResourceInvokeController {

    @Autowired
    private ResourceInvokeService resourceInvokeService;

    @RequestMapping(value = "/invoke", method = RequestMethod.POST)
    @ApiOperation(value = "接口调用",notes = "接口调用")
    public Resp<HttpResponse> invoke(@RequestBody Req<InvokeVo> req){
        InvokeVo invokeVo= req.getData();
        log.info("ResourceInvokeController invoke start,resourceId={}",invokeVo.getResourceId());
        Resp<HttpResponse>  resp=resourceInvokeService.invoke(invokeVo);
        HttpResponse httpResponse = resp.getData();
        if(resp.isSuccess() && httpResponse.isBeFile()) {
            //结果为文件
            String filename = httpResponse.getFilename();
            log.info("ResourceInvokeController invoke result is file ,filename={}",filename);
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            Map<String,String> headerMap = httpResponse.getHeaders();
            for(Map.Entry<String,String> entry: headerMap.entrySet()) {
                response.setHeader(entry.getKey(),entry.getValue());
            }
            response.setHeader("filename",filename);
            if(StringUtils.isNotBlank(httpResponse.getContentType())) {
                response.setContentType(httpResponse.getContentType());
            }
            try {
                byte [] data = ( byte [])httpResponse.getBody();
                response.getOutputStream().write(data);
                response.getOutputStream().flush();
            } catch (IOException e) {
                log.error("ResourceInvokeController invoke file deal error,",e);
            }
            return null;
        }
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
