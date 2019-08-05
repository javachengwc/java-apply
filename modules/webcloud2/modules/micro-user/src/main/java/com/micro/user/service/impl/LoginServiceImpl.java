package com.micro.user.service.impl;

import com.micro.user.constant.CommonConstant;
import com.micro.user.constant.JwtConstant;
import com.micro.user.model.pojo.User;
import com.micro.user.model.req.LoginReq;
import com.micro.user.model.vo.LoginVo;
import com.micro.user.service.LoginService;
import com.micro.user.service.UserService;
import com.micro.webcore.util.JwtTokenUtil;
import com.micro.webcore.util.RedisUtil;
import com.util.base.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    private static Logger logger= LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserService userService;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    //登录
    @Override
    public LoginVo login(User user, LoginReq loginReq) {
        Long userId = user.getId();
        String mobile = user.getMobile();
        logger.info("LoginServiceImpl login start,userId={},mobile={}",userId,mobile);
        String randomStr = RandomUtil.getRandomString(6);
        String token = JwtTokenUtil.generateToken(userId,mobile, randomStr);

        String tokenKey = CommonConstant.LOGIN_TOKEN_PRE+userId;
        RedisUtil.set(redisTemplate,tokenKey,token,JwtConstant.TOKEN_EEPIRATION);

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        response.setHeader(JwtConstant.HEADER_TOKEN, token);

        Date now = new Date();
        User userUpt = new User();
        userUpt.setId(user.getId());
        userUpt.setLastLoginTime(now);
        userUpt.setModifiedTime(now);
        userService.updateById(userUpt);

        LoginVo loginVo = new LoginVo(userId, user.getMobile());
        return loginVo;
    }

    //登出
    @Override
    public boolean logout(Long userId) {
        logger.info("LoginServiceImpl logout start, userId={}",userId);
        String tokenKey = CommonConstant.LOGIN_TOKEN_PRE+userId;
        RedisUtil.remove(redisTemplate,tokenKey);
        return true;
    }

    //检查登录态
    @Override
    public boolean checkLogin(Long userId) {
        String tokenKey = CommonConstant.LOGIN_TOKEN_PRE+userId;
        Object cacheObj = RedisUtil.get(redisTemplate,tokenKey);
        if(cacheObj!=null) {
            return true;
        }
        return false;
    }

    //检查token登录
    public boolean checkTokenLogin(Long userId,String token) {
        String tokenKey = CommonConstant.LOGIN_TOKEN_PRE+userId;
        Object cacheObj = RedisUtil.get(redisTemplate,tokenKey);
        String cacheToken = cacheObj==null?null:cacheObj.toString();
        if(StringUtils.isNotBlank(cacheToken) && cacheToken.equals(token)) {
            return true;
        }
        return false;
    }

    //延期token
    public boolean deferToken(Long userId,String token) {
        String tokenKey = CommonConstant.LOGIN_TOKEN_PRE+userId;
        Object cacheObj = RedisUtil.get(redisTemplate,tokenKey);
        String cacheToken =cacheObj==null?null:cacheObj.toString();
        if(cacheToken!=null && !cacheToken.equals(token)) {
            logger.info("LoginServiceImpl deferToken 缓存token与入参token不一样,userId={}",userId);
        }
        RedisUtil.set(redisTemplate,tokenKey,token,JwtConstant.TOKEN_EEPIRATION);
        return true;
    }

    //刷新token
    public String refreshToken(Long userId) {
        User user = userService.getById(userId);
        String mobile =user.getMobile();
        String randomStr = RandomUtil.getRandomString(6);
        String token = JwtTokenUtil.generateToken(userId,mobile, randomStr);

        String tokenKey = CommonConstant.LOGIN_TOKEN_PRE+userId;
        RedisUtil.set(redisTemplate,tokenKey,token,JwtConstant.TOKEN_EEPIRATION);
        return token;
    }

}
