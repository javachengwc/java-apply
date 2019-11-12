package com.micro.order.service.remote;

import com.micro.order.model.third.UserWalletReq;
import com.model.base.Req;
import com.model.base.Resp;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "micro-user")
public interface UserWalletService {

    //扣减用户钱包金额
    @Hmily
    @RequestMapping("/user/wallet/decreaseAmount")
    public Resp<Void> decreaseAmount(@RequestBody Req<UserWalletReq> req);
}
