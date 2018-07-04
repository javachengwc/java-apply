package com.shop.user.controller;

import com.shop.base.model.Req;
import com.shop.base.model.ReqHeader;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.model.vo.LoginVo;
import com.shop.user.api.rest.LoginResource;
import com.shop.user.model.ClientDevice;
import com.shop.user.service.UserService;
import com.util.web.RequestUtil;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        //获取request,response
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //HttpServletResponse response = requestAttributes.getResponse();
        ClientDevice clientDevice=tipClientDevice(request,reqHeader);
        //登录操作
        resp= userService.login(loginVo,clientDevice);
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
        ReqHeader reqHeader = req.getHeader();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        ClientDevice clientDevice=tipClientDevice(request,reqHeader);
        userService.logout(token,clientDevice);
        return resq;
    }

    @ApiOperation(value = "检测用户登录有效性", notes = "检测用户登录有效性")
    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
    public Resp<Void> checkLogin(@RequestBody Req<Void> req) {
        Resp<Void> resp = new Resp<Void>();
        String token = req.getHeader()==null?null:req.getHeader().getToken();
        if(StringUtils.isBlank(token)) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("登录已过期");
            return resp;
        }
        resp = userService.checkLogin(token);
        return resp;
    }

    private ClientDevice tipClientDevice( HttpServletRequest request,ReqHeader reqHeader) {
        String ip = RequestUtil.getIpFromRequest(request);

        ClientDevice clientDevice= new ClientDevice();
        clientDevice.setIp(ip);
        clientDevice.setApp(reqHeader.getApp());
        clientDevice.setAppVersion(reqHeader.getAppVersion());
        clientDevice.setDeviceOs(reqHeader.getDeviceOs());
        clientDevice.setDeviceOsVersion(reqHeader.getDeviceOsVersion());
        clientDevice.setDeviceToken(reqHeader.getDeviceToken());
        return  clientDevice;
    }
}
