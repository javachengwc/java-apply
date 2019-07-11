package com.micro.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.micro.user.dao.plus.UserPlusMapper;
import com.micro.user.enums.UserStatuEnum;
import com.micro.user.model.pojo.User;
import com.micro.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserPlusMapper, User> implements UserService {

    public User queryByMobile(String mobile) {
        User user =this.baseMapper.selectOne(new QueryWrapper<User>().eq("mobile",mobile));
        return user;
    }

    //根据第三方账号查询用户
    public User queryByThirdAccount(String thirdAccount,Integer thirdType) {
        //TODO
        return null;
    }

    //检查用户是否正常(禁用的用户返回false)
    public boolean checkUserNormal(User user) {
        if (null == user) {
            return false;
        }
        if (user.getStatu() == UserStatuEnum.FORBID.getValue().intValue()) {
            //禁用中
            return false;
        }
        return true;
    }
}
