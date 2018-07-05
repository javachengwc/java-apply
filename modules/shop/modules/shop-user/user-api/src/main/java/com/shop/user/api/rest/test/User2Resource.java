package com.shop.user.api.rest.test;


import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.user.api.model.UserInfo;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "测试用户接口")
@RequestMapping(value="/test/user2")
public interface User2Resource {

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = false, dataType = "Long", paramType = "query"),
    })
    @RequestMapping(value="/getUserInfo",method= RequestMethod.POST)
    public UserInfo getUserInfo(@RequestParam(value = "userId",required = false) Long userId);


    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = false, dataType = "Long", paramType = "query"),
    })
    @RequestMapping(value="/getUserInfo2",method= RequestMethod.GET)
    public Resp<UserInfo> getUserInfo2(@RequestParam(value = "userId",required = false) Long userId);

    @ApiOperation(value = "查询用户", notes = "查询用户")
    //@ApiImplicitParam(name = "req", value = "请求body", required = true, dataType = "Req<UserInfo>")
    @RequestMapping(value = "/queryByCdn", method = RequestMethod.POST ,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Resp<UserInfo> queryByCdn(@ApiParam(name="req",value="请求body",required = true) @RequestBody Req<UserInfo> req, Errors errors);
}
