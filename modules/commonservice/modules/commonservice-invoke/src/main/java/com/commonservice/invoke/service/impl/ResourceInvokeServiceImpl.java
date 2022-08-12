package com.commonservice.invoke.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.commonservice.invoke.dao.ResourceInvokeMapper;
import com.commonservice.invoke.dao.ext.ResourceInvokeDao;
import com.commonservice.invoke.model.entity.AccessResource;
import com.commonservice.invoke.model.entity.ResourceHeader;
import com.commonservice.invoke.model.entity.ResourceInvoke;
import com.commonservice.invoke.model.param.ResourceInvokeQuery;
import com.commonservice.invoke.model.vo.InvokeVo;
import com.commonservice.invoke.service.AccessResourceService;
import com.commonservice.invoke.service.ResourceHeaderService;
import com.commonservice.invoke.service.ResourceInvokeService;
import com.commonservice.invoke.util.HttpProxy;
import com.commonservice.invoke.util.HttpResponse;
import com.model.base.PageVo;
import com.model.base.Resp;
import com.util.JsonUtil;
import com.util.base.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ResourceInvokeServiceImpl extends ServiceImpl<ResourceInvokeMapper, ResourceInvoke>
        implements ResourceInvokeService {

    @Autowired
    private ResourceInvokeDao resourceInvokeDao;

    @Autowired
    private AccessResourceService accessResourceService;

    @Autowired
    private ResourceHeaderService resourceHeaderService;

    //接口调用
    public Resp<Object> invoke(InvokeVo invokeVo) {
        Long resourceId = invokeVo.getResourceId();
        log.info("ResourceInvokeServiceImpl invoke start,resourceId={},invokeVo={}", resourceId, JsonUtil.obj2Json(invokeVo));
        //准备header
        this.prepareHeader(invokeVo);
        AccessResource accessResource= accessResourceService.getById(resourceId);
        String url = accessResource.getResourceLink();
        String httpMethod = accessResource.getHttpMethod();
        String contentType = accessResource.getContentType();
        Resp<Object> resp = new Resp<Object>();
        HttpResponse httpResponse = null;
        String errorMsg = null;
        //产生调用并保存
        ResourceInvoke invokeInfo = genResourceInvoke(accessResource,invokeVo);
        this.save(invokeInfo);
        try {
            httpResponse = HttpProxy.invoke(url,httpMethod,invokeVo.getParams(),invokeVo.getHeaders(),invokeVo.getCookies(),contentType);
            resp.setData(httpResponse);
            log.info("ResourceInvokeServiceImpl invoke httpProxy end ,resourceId={},url={}",resourceId,url);
        } catch (Exception e) {
            log.error("ResourceInvokeServiceImpl invoke error,url={},httpMethod={},contentType={},invokeVo={}",
                    url,httpMethod,contentType,invokeVo,e);
            errorMsg = StringUtils.isBlank(e.getMessage()) ? e.getClass().getSimpleName(): e.getMessage();
            resp = Resp.error(errorMsg);
        }

        //更新调用结果
        ResourceInvoke invokeResult = genInvokeResult(httpResponse,errorMsg,invokeInfo.getInvokeTime());
        invokeResult.setId(invokeInfo.getId());
        this.updateById(invokeResult);
        return  resp;
    }

    //准备请求header
    private void prepareHeader(InvokeVo invokeVo) {
        Long resourceId = invokeVo.getResourceId();
        List<ResourceHeader> rsHeaderList = resourceHeaderService.queryByResource(resourceId);
        if(rsHeaderList == null || rsHeaderList.size()<=0) {
            return;
        }
        Map<String,String> curHeaders = invokeVo.getHeaders();
        Map<String,String> curCookies = invokeVo.getCookies();
        for(ResourceHeader resourceHeader:rsHeaderList) {
            int type = resourceHeader.getType();
            String name = resourceHeader.getName();
            if(type==0) {
                //header
                if(curHeaders==null) {
                    curHeaders = new HashMap<String,String>();
                    invokeVo.setHeaders(curHeaders);
                }
                if(!curHeaders.containsKey(name)) {
                    //添加默认header
                    curHeaders.put(name,resourceHeader.getDefaultValue());
                }
            }
            if(type==1) {
                //cookie
                if(curCookies==null) {
                    curCookies = new HashMap<String,String>();
                    invokeVo.setCookies(curCookies);
                }
                if(!curCookies.containsKey(name)) {
                    //添加默认header
                    curCookies.put(name,resourceHeader.getDefaultValue());
                }
            }
        }
    }

    private ResourceInvoke genResourceInvoke(AccessResource accessResource,InvokeVo invokeVo) {
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
        resourceInvoke.setInvokeTime(now);
        resourceInvoke.setCreateTime(now);
        resourceInvoke.setModifyTime(now);
        return  resourceInvoke;
    }

    private ResourceInvoke genInvokeResult( HttpResponse httpResponse,String errorMsg,Date invokeTime) {
        Date now = new Date();
        ResourceInvoke resourceInvoke = new ResourceInvoke();
        resourceInvoke.setReturnTime(now);
        resourceInvoke.setCost(now.getTime()-invokeTime.getTime());

        String respData =( httpResponse==null || httpResponse.getBody()== null ) ? "" :
                ( httpResponse.isJson() ? JsonUtil.obj2Json(httpResponse.getBody()) : httpResponse.getBody().toString());
        respData= respData.length()>10000? respData.substring(0,10000): respData;
        resourceInvoke.setRespData(respData);

        resourceInvoke.setRespCode(httpResponse==null? -1 :httpResponse.getCode());
        resourceInvoke.setIsSuccess( (httpResponse==null || !httpResponse.isSuccess())? 0 : 1 );

        String errorMessage =( errorMsg== null ) ? "" : errorMsg;
        errorMessage= errorMessage.length()>200? errorMessage.substring(0,200): errorMessage;
        resourceInvoke.setErrorMessage(errorMessage);
        return resourceInvoke;
    }

    public PageVo<ResourceInvoke> page(ResourceInvokeQuery query) {
        log.info("ResourceInvokeServiceImpl page start ,query={}", JsonUtil.obj2Json(query));
        int pageNum = query.getPageNum();
        int pageSize = query.getPageSize();
        int total = resourceInvokeDao.countPage(query);

        PageVo<ResourceInvoke> page = new PageVo<ResourceInvoke>();
        page.setTotalCount(total);

        if(page.isBound(pageNum,pageSize)) {
            page.setList(Collections.EMPTY_LIST);
            return page;
        }

        if(StringUtils.isNotBlank(query.getOrderBy())) {
            query.setOrderBy(StringUtil.field2Col(query.getOrderBy()));
        }
        List<ResourceInvoke> list = resourceInvokeDao.listPage(query);
        page.setList(list);
        return page;
    }
}