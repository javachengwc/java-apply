package com.commonservice.invoke.dao.ext;

import com.commonservice.invoke.model.entity.ResourceInvoke;
import com.commonservice.invoke.model.param.ResourceInvokeQuery;

import java.util.List;

public interface ResourceInvokeDao {

    public int countPage(ResourceInvokeQuery query);

    public List<ResourceInvoke> listPage(ResourceInvokeQuery query);
}
