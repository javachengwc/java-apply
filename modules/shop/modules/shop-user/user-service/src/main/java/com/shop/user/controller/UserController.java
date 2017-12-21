package com.shop.user.controller;

import com.shop.user.api.model.UserInfo;
import com.shop.user.api.rest.UserResource;
import com.shop.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "用户接口")
@RestController
@RequestMapping(value="/user")
public class UserController implements UserResource{

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @RequestMapping(value="/getUserInfo",method= RequestMethod.GET)
    public UserInfo getUserInfo(@RequestParam("userId") Long userId){
        if(userId==null) {
            return null;
        }
        return userService.getUserInfo(userId);
    }
}
