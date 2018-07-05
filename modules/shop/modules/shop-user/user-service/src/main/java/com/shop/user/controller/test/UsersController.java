package com.shop.user.controller.test;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.rest.test.UserResCtrl;
import com.shop.user.api.rest.test.UsersResource;
import com.shop.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "测试用户接口")
@RestController
@RequestMapping(value="/test/user")
public class UsersController implements UsersResource,UserResCtrl{

    private static Logger logger= LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @RequestMapping(value="/getUserInfo",method= RequestMethod.POST)
    public UserInfo getUserInfo(@RequestParam(value = "userId",required = false) Long userId){
        logger.info("UserController getUserInfo start,userId={}",userId);
        if(userId==null) {
            return null;
        }
        return userService.getUserInfo(userId);
    }


    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @RequestMapping(value="/getUserInfo2",method= RequestMethod.GET)
    public Resp<UserInfo> getUserInfo2(@RequestParam(value = "userId",required = false) Long userId) {
        logger.info("UserController getUserInfo2 start,userId={}",userId);

        Resp<UserInfo> rep= new Resp<UserInfo>();
        if(userId==null) {
            rep.getHeader().setCode(RespHeader.FAIL);
            return rep;
        }
        UserInfo userInfo = userService.getUserInfo(userId);
        rep.setData(userInfo);
        rep.getHeader().setCode(RespHeader.SUCCESS);
        return rep;
    }

    @ApiOperation(value = "获取用户信息3", notes = "获取用户信息3")
    @RequestMapping(value="/queryUserInfo3",method= RequestMethod.POST)
    public Resp<UserInfo> queryUserInfo3(@RequestBody @Validated Req<Long> req, Errors errors) {
        logger.info("UserController queryUserInfo3 start,req={}",req);
        Long userId =req.getData();
        Resp<UserInfo> rep= new Resp<UserInfo>();
        if(userId==null) {
            rep.getHeader().setCode(RespHeader.FAIL);
            return rep;
        }
        UserInfo userInfo = userService.getUserInfo(userId);
        rep.setData(userInfo);
        rep.getHeader().setCode(RespHeader.SUCCESS);
        return rep;
    }
}
