package com.micro.webcore.feign;

import com.model.base.Req;
import com.model.base.Resp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "${application.userService:userService}")
public interface LoginCheckService {

    @PostMapping("/checkLogin")
    public Resp<Void> checkLogin(Req<Void> req);
}
