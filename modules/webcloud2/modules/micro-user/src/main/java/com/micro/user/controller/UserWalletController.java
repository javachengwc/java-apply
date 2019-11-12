package com.micro.user.controller;

import com.micro.user.model.pojo.UserWallet;
import com.micro.user.model.req.UserWalletReq;
import com.micro.user.model.vo.UserWalletVo;
import com.micro.user.service.UserWalletService;
import com.micro.webcore.annotation.AppLogin;
import com.model.base.Req;
import com.model.base.Resp;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value="用户钱包接口",description="用户钱包接口")
@RequestMapping("/user/wallet")
@RestController
public class UserWalletController {

    private static Logger logger= LoggerFactory.getLogger(UserWalletController.class);

    @Autowired
    private UserWalletService userWalletService;

    @ApiOperation(value = "根据id查询用户钱包", notes = "根据id查询用户钱包")
    @RequestMapping(value = "/queryUserWalletById", method = RequestMethod.POST)
    @AppLogin
    public Resp<UserWalletVo> queryUserWalletById(@RequestBody Req<Long> req) {
        Long userWalletId = req.getData();
        if(userWalletId==null) {
            return Resp.error("参数错误");
        }
        UserWallet userWallet  = userWalletService.getById(userWalletId);
        if(userWallet==null) {
            return Resp.error("无用户信息");
        }
        UserWalletVo userVo = TransUtil.transEntity(userWallet,UserWalletVo.class);
        return Resp.data(userVo);
    }

    @ApiOperation(value = "扣减金额", notes = "扣减金额")
    @RequestMapping(value = "/decreaseAmount", method = RequestMethod.POST)
    public Resp<Void> decreaseAmount(@RequestBody Req<UserWalletReq> req) {
        UserWalletReq userWalletReq = req.getData();
        Long userId = userWalletReq==null?null:userWalletReq.getUserId();
        Long changeAmount = userWalletReq==null?null:userWalletReq.getChangeAmount();
        logger.info("UserWalletController decreaseAmount start,userId={},changeAmount={}",userId,changeAmount);
        if(userId==null || changeAmount==null) {
            return Resp.error("参数错误");
        }
        userWalletService.decreaseAmount(userWalletReq);
        return Resp.success();
    }
}
