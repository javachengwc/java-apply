package com.shop.user.api.rest;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.user.api.model.vo.LoginVo;
import com.shop.user.api.model.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api("用户登录接口")
public interface LoginResource {

    @ApiOperation(value = "登录", notes = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Resp<UserInfo> login(@Validated @RequestBody Req<LoginVo> req, Errors errors);

    @ApiOperation(value = "登出", notes = "登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Resp<Void> logout(@RequestBody Req<Void> req);

    @ApiOperation(value = "检测用户登录有效性", notes = "检测用户登录有效性")
    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
    public Resp<Integer> checkLogin(@RequestBody Req<Void> req);
}
