package com.shop.user.service.impl;

import com.shop.user.dao.mapper.ShopUserMapper;
import com.shop.user.model.UserInfo;
import com.shop.user.model.pojo.ShopUser;
import com.shop.user.service.UserService;
import com.util.date.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private ShopUserMapper shopUserMapper;

    public ShopUser getById(Long userId) {

        return shopUserMapper.selectByPrimaryKey(userId);

    }

    public UserInfo getUserInfo(Long userId) {

        ShopUser shopUser = getById(userId);
        if(shopUser==null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(shopUser,userInfo);

        //注册时间
        Date regTime = userInfo.getRegTime();
        String regTimeStr=transDateTimeStr(regTime);
        userInfo.setRegTimeStr(regTimeStr);

        //最后登陆时间
        Date lastLoginTime = userInfo.getLastLoginTime();
        String lastLoginTimeStr=transDateTimeStr(lastLoginTime);
        userInfo.setLastLoginTimeStr(lastLoginTimeStr);
        return userInfo;
    }


    public String transDateTimeStr(Date date) {
        if(date==null) {
            return "";
        }
        return DateUtil.formatDate(date,DateUtil.FMT_YMD_HMS);
    }
}
