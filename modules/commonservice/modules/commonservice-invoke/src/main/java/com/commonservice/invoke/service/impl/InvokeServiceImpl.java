package com.commonservice.invoke.service.impl;

import com.commonservice.invoke.model.vo.InvokeVo;
import com.commonservice.invoke.service.InvokeService;
import com.model.base.Resp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class InvokeServiceImpl implements InvokeService {

    private static Logger logger= LoggerFactory.getLogger(InvokeServiceImpl.class);

    public Resp<Void> invoke(InvokeVo invokeVo) {
        return null;
    }

}
