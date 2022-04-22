package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.ResourceInvokeMapper;
import com.commonservice.invoke.model.entity.AccessResource;
import com.commonservice.invoke.model.entity.ResourceInvoke;
import com.commonservice.invoke.model.vo.InvokeVo;
import com.commonservice.invoke.model.vo.ResourceInvokeVo;
import com.commonservice.invoke.service.AccessResourceService;
import com.commonservice.invoke.service.ResourceInvokeService;
import com.commonservice.invoke.util.HttpProxy;
import com.commonservice.invoke.util.HttpResponse;
import com.model.base.Resp;
import com.util.JsonUtil;
import com.util.page.Page;
import com.util.page.PageQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class ResourceInvokeServiceImpl extends ServiceImpl<ResourceInvokeMapper, ResourceInvoke>
        implements ResourceInvokeService {

    @Autowired
    private AccessResourceService accessResourceService;

    //接口调用
    public Resp<Object> invoke(InvokeVo invokeVo) {
        Long resourceId = invokeVo.getResourceId();
        log.info("ResourceInvokeServiceImpl invoke start,resourceId={},invokeVo={}", resourceId, JsonUtil.obj2Json(invokeVo));
        AccessResource accessResource= accessResourceService.getById(resourceId);
        String url = accessResource.getResourceLink();
        String httpMethod = accessResource.getHttpMethod();
        String contentType = accessResource.getContentType();
        Resp<Object> resp = new Resp<Object>();
        HttpResponse httpResponse = null;
        String errorMsg = null;
        try {
            httpResponse = HttpProxy.invoke(url,httpMethod,invokeVo.getParams(),invokeVo.getHeaders(),contentType);
            resp.setData(httpResponse);
            log.info("ResourceInvokeServiceImpl invoke httpProxy end ,resourceId={},url={}",resourceId,url);
        } catch (Exception e) {
            log.error("ResourceInvokeServiceImpl invoke error,url={},httpMethod={},contentType={},invokeVo={}",
                    url,httpMethod,contentType,invokeVo);
            errorMsg = StringUtils.isBlank(e.getMessage()) ? e.getClass().getSimpleName(): e.getMessage();
            resp = Resp.error(errorMsg);
        }

        //产生调用结果并保存
        ResourceInvoke resourceInvokeInfo = genResourceInvoke(accessResource,invokeVo,httpResponse,errorMsg);
        this.save(resourceInvokeInfo);
        return  resp;
    }

    private ResourceInvoke genResourceInvoke(AccessResource accessResource,InvokeVo invokeVo,
                                             HttpResponse httpResponse,String errorMsg) {
        Date now = new Date();
        ResourceInvoke resourceInvoke = new ResourceInvoke();
        resourceInvoke.setResourceId(accessResource.getId());
        resourceInvoke.setResourceName(accessResource.getName());
        resourceInvoke.setResourceLink(accessResource.getResourceLink());
        resourceInvoke.setHttpMethod(accessResource.getHttpMethod());
        resourceInvoke.setContentType(accessResource.getContentType());

        String headerJson = (invokeVo.getHeaders()==null || invokeVo.getHeaders().size()<=0)?"":JsonUtil.obj2Json(invokeVo.getHeaders());
        headerJson= headerJson.length()>500? headerJson.substring(0,500): headerJson;
        resourceInvoke.setReqHeader(headerJson);

        String paramJson = (invokeVo.getParams()==null || invokeVo.getParams().size()<=0)?"":JsonUtil.obj2Json(invokeVo.getParams());
        paramJson= paramJson.length()>1000? paramJson.substring(0,1000): paramJson;
        resourceInvoke.setReqData(paramJson);

        String respData =( httpResponse==null || httpResponse.getBody()== null ) ? "" : httpResponse.getBody();
        respData= respData.length()>2000? respData.substring(0,2000): respData;
        resourceInvoke.setRespData(respData);

        resourceInvoke.setRespCode(httpResponse==null? -1 :httpResponse.getCode());
        resourceInvoke.setIsSuccess( (httpResponse==null || !httpResponse.isSuccess())? 0 : 1 );

        String errorMessage =( errorMsg== null ) ? "" : errorMsg;
        errorMessage= errorMessage.length()>200? errorMessage.substring(0,200): errorMessage;
        resourceInvoke.setErrorMessage(errorMessage);

        resourceInvoke.setCreateTime(now);
        resourceInvoke.setModifyTime(now);
        return  resourceInvoke;
    }

    public Page<ResourceInvoke> page(PageQuery<ResourceInvokeVo> query) {

        return null;
    }
}