package com.commonservice.push.service;

import com.commonservice.push.model.base.Resp;
import com.commonservice.push.model.vo.PushMessageVo;

public interface PushService {

    //推送消息
    public Resp<Void> push(PushMessageVo messageVo);
}
