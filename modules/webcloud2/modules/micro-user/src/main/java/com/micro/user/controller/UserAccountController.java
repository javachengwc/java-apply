package com.micro.user.controller;

import com.micro.user.model.req.PwdReq;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import io.swagger.annotations.Api;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="用户账户相关接口",description="用户账户相关接口")
@RequestMapping("/user/account")
@RestController
public class UserAccountController {

    @PostMapping("modifyPwd")
    public Resp<Void> modifyPwd(@Validated @RequestBody Req<PwdReq> req, Errors errors) {
        Resp<Void> resp = new Resp<>();
        return resp;
    }

    @PostMapping("findPwd")
    public Resp<Void> findPwd(@Validated @RequestBody Req<PwdReq> req, Errors errors) {
        Resp<Void> resp = new Resp<>();
        return resp;
    }
}
