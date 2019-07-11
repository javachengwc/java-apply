package com.micro.user.controller;

import com.micro.user.annotation.AppLogin;
import com.micro.user.context.LoginContext;
import com.micro.user.model.LoginUser;
import com.micro.user.model.pojo.User;
import com.micro.user.model.req.PwdReq;
import com.micro.user.service.SmsService;
import com.micro.user.service.UserAccountService;
import com.micro.user.service.UserService;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static Logger logger= LoggerFactory.getLogger(UserAccountController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private SmsService smsService;

    @PostMapping("/modifyPasswd")
    @ApiOperation("修改密码")
    @AppLogin
    public Resp<Void> modifyPasswd(@Validated @RequestBody Req<PwdReq> req, Errors errors) {
        PwdReq pwdReq = req.getData();
        String newPasswd = pwdReq.getPasswd();
        String confirmPasswd = pwdReq.getConfirmPasswd();
        String oldPasswd= pwdReq.getOldPasswd();
        if (StringUtils.isBlank(oldPasswd) ) {
            return Resp.error("原密码不能为空");
        }
        if (StringUtils.isBlank(newPasswd) ) {
            return Resp.error("新密码不能为空");
        }
        if (StringUtils.isBlank(confirmPasswd) || !newPasswd.equals(confirmPasswd) ) {
            return Resp.error("新密码与确认密码不一致");
        }
        LoginUser loginUser = LoginContext.getLoginUser();
        Long userId = loginUser.getUserId();
        pwdReq.setUserId(userId);
        User user=userService.getById(userId);
        if(user==null) {
            return Resp.error("用户不存在");
        }
        logger.info("UserAccountController modifyPasswd userId={}",userId);
        boolean accountCheck =userAccountService.checkAccount(user,oldPasswd);
        if(!accountCheck) {
            return Resp.error("原密码不正确");
        }
        boolean rt =userAccountService.modifyPasswd(user,pwdReq);
        return rt ? Resp.success() : Resp.error("修改失败,请稍后再试");
    }

    @PostMapping("/resetPasswd")
    @ApiOperation("重置密码")
    public Resp<Void> resetPasswd(@Validated @RequestBody Req<PwdReq> req, Errors errors) {
        PwdReq pwdReq = req.getData();
        String newPasswd = pwdReq.getPasswd();
        String confirmPasswd = pwdReq.getConfirmPasswd();
        if (StringUtils.isBlank(newPasswd)) {
            return Resp.error("请输入密码");
        }
        if ( StringUtils.isBlank(confirmPasswd) || !newPasswd.equals(confirmPasswd) ) {
            return Resp.error("新密码与确认密码不一致");
        }
        String mobile = pwdReq.getMobile();
        User user=userService.queryByMobile(mobile);
        if(user==null) {
            return Resp.error("用户不存在");
        }
        String code = pwdReq.getCode();
        boolean codeCheck = smsService.checkCode(mobile,code);
        if(!codeCheck) {
            return Resp.error("验证码错误");
        }
        boolean rt =userAccountService.modifyPasswd(user,pwdReq);
        return rt ? Resp.success() : Resp.error("密码重置失败,请稍后再试");
    }
}
