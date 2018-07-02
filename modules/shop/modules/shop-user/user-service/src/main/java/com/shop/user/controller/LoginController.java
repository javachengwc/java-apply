package com.shop.user.controller;

import com.shop.base.model.Req;
import com.shop.base.model.ReqHeader;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.model.vo.LoginVo;
import com.shop.user.api.rest.LoginResource;
import com.shop.user.model.LoginInfo;
import com.shop.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("用户登录接口")
@RestController
public class LoginController  implements LoginResource {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录", notes = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Resp<UserInfo> login(@Validated @RequestBody Req<LoginVo> req, Errors errors) {
        Resp<UserInfo> resp = new Resp<UserInfo>();
        LoginVo loginVo = req.getData();
        ReqHeader reqHeader = req.getHeader();
        //这里暂时不验证验证码
        LoginInfo loginInfo= new LoginInfo(loginVo.getMobile(),loginVo.getCaptcha());
        loginInfo.setApp(reqHeader.getApp());
        loginInfo.setAppVersion(reqHeader.getAppVersion());
        loginInfo.setDeviceOs(reqHeader.getDeviceOs());
        loginInfo.setDeviceOsVersion(reqHeader.getDeviceOsVersion());
        loginInfo.setDeviceToken(reqHeader.getDeviceToken());

        UserInfo userInfo= userService.login(loginInfo);
        if (userInfo==null) {
            //登录失败
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("登录失败");
            return  resp;
        }
        resp.setData(userInfo);
        return  resp;
    }

    @ApiOperation(value = "登出", notes = "登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Resp<Void> logout(@RequestBody Req<Void> req) {
        String token = req.getHeader()==null?null:req.getHeader().getToken();
        Resp<Void> resq= new Resp<Void>();
        if(StringUtils.isBlank(token)) {
            return resq;
        }
        userService.logout(token);
        return resq;
    }

    @ApiOperation(value = "检测用户登录有效性", notes = "检测用户登录有效性")
    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
    public Resp<Integer> checkLogin(@RequestBody Req<Void> req) {
        String token = req.getHeader()==null?null:req.getHeader().getToken();
        Resp<Integer> resq= new Resp<Integer>();
        boolean rt = userService.checkLogin(token);
        int rtInt = rt?1:0;
        resq.setData(rtInt);
        return resq;
    }
}
