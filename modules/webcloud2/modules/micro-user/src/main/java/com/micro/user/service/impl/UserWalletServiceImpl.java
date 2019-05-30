package com.micro.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.user.dao.mapper.UserWalletMapper;
import com.micro.user.model.pojo.UserWallet;
import com.micro.user.service.UserWalletService;
import org.springframework.stereotype.Service;

@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService {
}
