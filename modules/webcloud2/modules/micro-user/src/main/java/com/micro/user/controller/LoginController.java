package com.micro.user.controller;

import com.micro.user.model.req.LoginReq;
import com.micro.user.model.vo.LoginVo;
import com.micro.user.service.LoginService;
import com.micro.user.service.UserService;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(value="登录接口",description="登录接口")
@RestController
public class LoginController {

    public static Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Resource
    private LoginService loginService;

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Resp<LoginVo> login(@Validated @RequestBody Req<LoginReq> req, Errors errors) {
        LoginReq loginReq = req.getData();
        logger.info("LoginController login start,loginReq={}", loginReq);
        Resp<LoginVo> resp = new Resp<LoginVo>();
        try {
        } catch (Exception e) {
        }
        return  resp;
    }

    @PostMapping("/logout")
    public Resp<Void> logout(@RequestBody Req<Void> req) {
        logger.info("LoginController logout start...............");
        Resp<Void> resp = new Resp<>();
        try {
        } catch (Exception e) {
        }
        return  resp;
    }

    @PostMapping("/checkLogin")
    public Resp<Void> checkLogin(@RequestBody @Valid Req<Void> req) {
        logger.info("LoginController checkLogin start...............");
        Resp<Void> resp = new Resp<>();
        try {
        } catch (Exception e) {
        }
        return  resp;
    }
}
