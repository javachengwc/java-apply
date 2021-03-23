package com.commonservice.invoke.service;

import com.commonservice.invoke.model.InvokeVo;
import com.model.base.Resp;

public interface InvokeService {

    //调用
    public Resp<Void> invoke(InvokeVo invokeVo);
}
