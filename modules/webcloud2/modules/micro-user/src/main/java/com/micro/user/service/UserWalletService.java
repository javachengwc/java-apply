package com.micro.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.micro.user.model.pojo.UserWallet;
import com.micro.user.model.req.UserWalletReq;
import org.dromara.hmily.annotation.Hmily;

public interface UserWalletService extends IService<UserWallet> {

    public boolean decreaseAmount(UserWalletReq userWalletReq);
}
