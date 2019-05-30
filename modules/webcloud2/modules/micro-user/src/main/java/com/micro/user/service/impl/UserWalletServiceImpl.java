package com.micro.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.user.dao.mapper.UserWalletMapper;
import com.micro.user.model.pojo.UserWallet;
import com.micro.user.model.req.UserWalletReq;
import com.micro.user.service.UserWalletService;
import org.dromara.hmily.annotation.Hmily;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService {

    private static Logger logger= LoggerFactory.getLogger(UserWalletServiceImpl.class);

    @Hmily(confirmMethod = "confirmDec", cancelMethod = "cancelDec")
    public boolean decreaseAmount(UserWalletReq userWalletReq) {
        logger.info("UserWalletServiceImpl decreaseAmount start,userWalletReq={}",userWalletReq);
        return true;
    }

    public boolean confirmDec(UserWalletReq userWalletReq) {
        logger.info("UserWalletServiceImpl confirmDec start,userWalletReq={}",userWalletReq);
        return true;
    }

    public boolean cancelDec(UserWalletReq userWalletReq) {
        logger.info("UserWalletServiceImpl cancelDec start,userWalletReq={}",userWalletReq);
        return true;
    }
}
