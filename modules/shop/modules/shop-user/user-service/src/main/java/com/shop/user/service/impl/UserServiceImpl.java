package com.shop.user.service.impl;

import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.base.util.TransUtil;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.model.vo.LoginVo;
import com.shop.user.dao.mapper.ShopUserMapper;
import com.shop.user.enums.UserComefromEnum;
import com.shop.user.enums.UserStatuEnum;
import com.shop.user.model.ClientDevice;
import com.shop.user.model.Token;
import com.shop.user.model.pojo.ShopUser;
import com.shop.user.model.pojo.ShopUserExample;
import com.shop.user.model.pojo.UserLogin;
import com.shop.user.service.UserLoginService;
import com.shop.user.service.UserService;
import com.shop.user.service.manager.TokenManager;
import com.util.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private static Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    private static String ACCOUNT_REMIND="您的账号于%s在另一台设备上进行了登录。如非本人操作，则手机验证码可能泄露。";

    @Autowired
    private ShopUserMapper shopUserMapper;

    @Autowired
    private UserLoginService userLoginService;

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

    public ShopUser queryByMobile(String mobile) {
        ShopUserExample example = new ShopUserExample();
        ShopUserExample.Criteria criteria =example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<ShopUser> list = shopUserMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return list.get(0);
        }
        return null;
    }

    public Resp<UserInfo> login(LoginVo loginVo, ClientDevice clientDevice) {
        String mobile = loginVo.getMobile();
        logger.info("UserServiceImpl login start,mobile={}",mobile);
        Resp<UserInfo> resp = new Resp<UserInfo>();
        //String mobileMd5 = MD5.getMD5(mobile);
        ShopUser user = queryByMobile(mobile);
        Date now = new Date();
        if (user == null) {
            //用户第一次登录,创建用户
            user= createUser(mobile,clientDevice);
        } else {
            if (UserStatuEnum.BANNED.getValue() == user.getStatu().intValue()) {
                //禁用状态
                resp.getHeader().setCode(RespHeader.FAIL);
                resp.getHeader().setMsg("账号被禁用");
                return resp;
            }
            user.setLastLoginTime(now);
            user.setModifiedTime(now);
        }

        //创建token
        Token token = TokenManager.getInstance().createToken(user);
        String tokenStr = token.getToken();
        user.setToken(tokenStr);
        user.setTokenExpireTime(token.getExpireTime());

        //记录登录
        UserLogin userLogin=userLoginService.recordUserLogin(user,clientDevice);
        user.setLastLoginId(userLogin.getId());

        //更新用户信息
        uptUser(user);
        UserInfo userInfo = TransUtil.transEntity(user,UserInfo.class);
        resp.setData(userInfo);
        return resp;
    }

    public ShopUser createUser(String mobile, ClientDevice clientDevice) {
        Date now = new Date();
        ShopUser user = new ShopUser();
        user.setCreateTime(now);
        user.setLastLoginTime(now);
        user.setRegTime(now);
        user.setModifiedTime(now);
        user.setStatu(UserStatuEnum.NORMAL.getValue());
        String app =clientDevice.getApp();
        int userComefrom = StringUtils.isBlank(app)? UserComefromEnum.PC.getValue():UserComefromEnum.APP.getValue();
        user.setRegComefrom(userComefrom);
        user.setName(mobile);
        user.setNickName(mobile);
        user.setMobile(mobile);

        user.setApp(app);
        user.setAppVersion(clientDevice.getAppVersion());
        user.setDeviceOs(clientDevice.getDeviceOs());
        user.setDeviceOsVersion(clientDevice.getDeviceOsVersion());
        user.setDeviceToken(clientDevice.getDeviceToken());
        shopUserMapper.insert(user);
        return user;
    }

    public Integer uptUser(ShopUser user) {
        int rt = shopUserMapper.updateByPrimaryKeySelective(user);
        return rt;
    }

    public boolean logout(String token,ClientDevice clientDevice) {
        Token tokenObj = TokenManager.getInstance().getToken(token);
        if (tokenObj != null) {
            Long userId = tokenObj.getUserId();
            TokenManager.getInstance().deleteToken(token);
            TokenManager.getInstance().deleteLatest(userId);
            userLoginService.recordUserLogout(userId,clientDevice);
            return true;
        }
        return false;
    }

    public Resp<Void> checkLogin(String token) {
        Resp<Void> resp = new Resp<Void>();
        Token tokenObj = TokenManager.getInstance().getToken(token);
        if (tokenObj == null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("登录已过期");
            return resp;
        }

        Long userId = tokenObj.getUserId();
        if (!TokenManager.getInstance().isLatest(token,userId)) {
            //此token非最后登录token
            //删掉此token
            TokenManager.getInstance().deleteToken(token);
            ShopUser user = getById(userId);
            Date lastLoginTime = user.getLastLoginTime();
            String lastLoginTimeStr = DateUtil.formatDate(lastLoginTime,DateUtil.FMT_YMD_HMS);
            //提示账号在其他地方登录
            String msg =String.format(ACCOUNT_REMIND,lastLoginTimeStr);
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg(msg);
            return resp;
        }
        return resp;
    }

    //根据token获取登录态用户Id
    public Long getLoginUserId(String token) {
        Token tokenObj = TokenManager.getInstance().getToken(token);
        if (tokenObj == null) {
            return null;
        }
        Long userId = tokenObj.getUserId();
        if (!TokenManager.getInstance().isLatest(token, userId)) {
            return null;
        }
        return userId;
    }
}
