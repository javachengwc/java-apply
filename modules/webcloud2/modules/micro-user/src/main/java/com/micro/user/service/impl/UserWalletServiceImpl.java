package com.micro.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.user.dao.mapper.UserWalletMapper;
import com.micro.user.model.pojo.UserWallet;
import com.micro.user.model.req.UserWalletReq;
import com.micro.user.service.UserWalletService;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.stereotype.Service;

@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService {

    @Hmily(confirmMethod = "confirmDec", cancelMethod = "cancelDec")
    public boolean decreaseAmount(UserWalletReq userWalletReq) {
        return true;
    }

    public boolean confirmDec(UserWalletReq userWalletReq) {
        return true;
    }

    public boolean cancelDec(UserWalletReq userWalletReq) {
        return true;
    }
}
