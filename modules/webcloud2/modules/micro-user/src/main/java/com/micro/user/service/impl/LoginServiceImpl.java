package com.micro.user.service.impl;

import com.micro.user.constant.CommonConstant;
import com.micro.user.constant.JwtConstant;
import com.micro.user.model.pojo.User;
import com.micro.user.model.req.LoginReq;
import com.micro.user.model.vo.LoginVo;
import com.micro.user.service.LoginService;
import com.micro.user.service.UserService;
import com.micro.user.util.JwtTokenUtil;
import com.micro.user.util.RedisUtil;
import com.util.base.RandomUtil;
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

    public boolean checkLoginByToken(String token) {
        return true;
    }
}
