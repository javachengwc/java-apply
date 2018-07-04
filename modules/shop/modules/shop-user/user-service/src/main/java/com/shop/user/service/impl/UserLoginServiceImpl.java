package com.shop.user.service.impl;

import com.shop.user.dao.mapper.UserLoginMapper;
import com.shop.user.enums.LoginTagEnum;
import com.shop.user.enums.UserComefromEnum;
import com.shop.user.model.ClientDevice;
import com.shop.user.model.pojo.ShopUser;
import com.shop.user.model.pojo.UserLogin;
import com.shop.user.model.pojo.UserLoginExample;
import com.shop.user.service.UserLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserLoginMapper userLoginMapper;

    //记录登录
    public UserLogin recordUserLogin(ShopUser user, ClientDevice clientDevice) {
        Date now = new Date();
        UserLogin userLogin =new UserLogin();
        userLogin.setUserId(user.getId());
        userLogin.setCreateTime(now);
        userLogin.setLoginTime(now);
        userLogin.setModifiedTime(now);
        userLogin.setLoginTag(LoginTagEnum.LOGIN.getValue());

        String app =clientDevice.getApp();
        int comefrom = StringUtils.isBlank(app)? UserComefromEnum.PC.getValue():UserComefromEnum.APP.getValue();
        userLogin.setLoginFrom(comefrom);

        userLogin.setIp(clientDevice.getIp());
        userLogin.setApp(app);
        userLogin.setAppVersion(clientDevice.getAppVersion());
        userLogin.setDeviceOs(clientDevice.getDeviceOs());
        userLogin.setDeviceOsVersion(clientDevice.getDeviceOsVersion());
        userLogin.setDeviceToken(clientDevice.getDeviceToken());

        userLoginMapper.insertSelective(userLogin);
        return userLogin;
    }

    //记录登出
    public UserLogin recordUserLogout(Long userId,ClientDevice clientDevice) {
        Date now = new Date();
        UserLogin userLogin =new UserLogin();
        userLogin.setUserId(userId);
        userLogin.setCreateTime(now);
        userLogin.setLogoutTime(now);
        userLogin.setModifiedTime(now);
        userLogin.setLoginTag(LoginTagEnum.LOGOUT.getValue());

        String app =clientDevice.getApp();
        int comefrom = StringUtils.isBlank(app)? UserComefromEnum.PC.getValue():UserComefromEnum.APP.getValue();
        userLogin.setLoginFrom(comefrom);

        userLogin.setIp(clientDevice.getIp());
        userLogin.setApp(app);
        userLogin.setAppVersion(clientDevice.getAppVersion());
        userLogin.setDeviceOs(clientDevice.getDeviceOs());
        userLogin.setDeviceOsVersion(clientDevice.getDeviceOsVersion());
        userLogin.setDeviceToken(clientDevice.getDeviceToken());

        userLoginMapper.insertSelective(userLogin);
        return userLogin;
    }

    //查询用户最后一次登录记录
    public UserLogin queryLatestLoginByUserId(Long userId) {
        UserLoginExample example = new UserLoginExample();
        UserLoginExample.Criteria criteria =example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andLoginTagEqualTo(LoginTagEnum.LOGIN.getValue());
        example.setOrderByClause(" login_time desc limit 1 ");
        List<UserLogin> list = userLoginMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }
}
