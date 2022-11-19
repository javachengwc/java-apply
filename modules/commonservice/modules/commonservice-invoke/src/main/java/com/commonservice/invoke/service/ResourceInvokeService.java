package com.commonservice.invoke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commonservice.invoke.model.entity.ResourceInvoke;
import com.commonservice.invoke.model.param.ResourceInvokeQuery;
import com.commonservice.invoke.model.vo.InvokeVo;
import com.commonservice.invoke.util.HttpResponse;
import com.model.base.PageVo;
import com.model.base.Resp;

public interface ResourceInvokeService extends IService<ResourceInvoke> {

    //接口调用
    Resp<HttpResponse> invoke(InvokeVo invokeVo);

    //分页查询
    PageVo<ResourceInvoke> page(ResourceInvokeQuery query);
}
