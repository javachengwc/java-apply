package com.shop.book.manage.controller;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.book.manage.enums.ApiCode;
import com.shop.book.manage.model.vo.LoginResultVo;
import com.shop.book.manage.model.vo.LoginVo;
import com.shop.book.manage.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "登录相关接口", description = "登录相关接口")
@RestController
public class LoginController {

    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/login")
    public Resp<LoginResultVo> login(@Validated @RequestBody Req<LoginVo> req) {
        LoginVo loginVo = req.getData();
        String mobile = loginVo==null?null:loginVo.getMobile();
        String captcha= loginVo==null?null:loginVo.getCaptcha();
        logger.info("LoginController login start,mobile={},captcha={}",mobile,captcha);
        if(StringUtils.isBlank(mobile)) {
            return Resp.error(ApiCode.PARAM_FAIL.getCode(),ApiCode.PARAM_FAIL.getMessage());
        }
        Resp<LoginResultVo> resp=loginService.login(loginVo);
        return resp;
    }

    @ApiOperation(value = "登出", notes = "登出")
    @PostMapping("/logout")
    public Resp<Void> logout(@RequestBody Req<Void> reqst) {
        loginService.logout();
        return Resp.success(null,"成功");
    }

}

