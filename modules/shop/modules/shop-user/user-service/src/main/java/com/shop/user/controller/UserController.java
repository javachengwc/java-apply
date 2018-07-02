package com.shop.user.controller;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.rest.UserResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api("用户接口")
@RequestMapping("/user")
public class UserController implements UserResource {

    @ApiOperation(value = "查询用户信息", notes = "查询用户信息")
    @RequestMapping(value = "/queryUserInfo", method = RequestMethod.POST)
    public Resp<UserInfo> queryUserInfo(@RequestBody Req<Void> reqst) {
        return null;
    }

    @ApiOperation(value = "根据手机号查询用户", notes = "根据手机号查询用户")
    @RequestMapping(value = "/queryUserByMobile", method = RequestMethod.POST)
    public Resp<UserInfo> queryUserByMobile(@RequestBody Req<String> reqst) {
        return null;
    }

    @ApiOperation(value = "根据id查询用户", notes = "根据id查询用户")
    @RequestMapping(value = "/queryUserById", method = RequestMethod.POST)
    public Resp<UserInfo> queryUserById(@RequestBody Req<Long> reqst) {
        return null;
    }

}
