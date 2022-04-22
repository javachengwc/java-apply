package com.commonservice.invoke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commonservice.invoke.model.entity.ResourceInvoke;
import com.commonservice.invoke.model.vo.InvokeVo;
import com.commonservice.invoke.model.vo.ResourceInvokeVo;
import com.model.base.Resp;
import com.util.page.Page;
import com.util.page.PageQuery;

public interface ResourceInvokeService extends IService<ResourceInvoke> {

    //接口调用
    Resp<Object> invoke(InvokeVo invokeVo);

    //分页查询
    Page<ResourceInvoke> page(PageQuery<ResourceInvokeVo> query);
}
