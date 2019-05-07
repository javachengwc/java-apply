package com.storefront.manage.controller;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.util.base.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "验证码接口", description = "验证码接口")
@RestController
@RequestMapping("/sms")
public class SmsController {

    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @PostMapping("/getCaptcha")
    public Resp<String> getCaptcha(@RequestBody Req<String> reqst, Errors errors) {
        Resp<String> resps = new Resp<String>();
        String captcha= RandomUtil.getRandomNumStr(6);
        return Resp.success(captcha,"成功");
    }
}
