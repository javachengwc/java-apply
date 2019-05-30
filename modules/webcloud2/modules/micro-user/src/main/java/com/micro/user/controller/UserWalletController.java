package com.micro.user.controller;

import com.micro.user.model.pojo.UserWallet;
import com.micro.user.model.vo.UserWalletVo;
import com.micro.user.service.UserWalletService;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.base.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("用户钱包接口")
@RequestMapping("/user/wallet")
@RestController
public class UserWalletController {

    @Autowired
    private UserWalletService userWalletService;

    @ApiOperation(value = "根据id查询用户钱包", notes = "根据id查询用户钱包")
    @RequestMapping(value = "/queryUserWalletById", method = RequestMethod.POST)
    public Resp<UserWalletVo> queryUserWalletById(@RequestBody Req<Long> req) {
        Resp<UserWalletVo> resp = new Resp<UserWalletVo>();
        Long userWalletId = req.getData();
        if(userWalletId==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        UserWallet userWallet  = userWalletService.getById(userWalletId);
        if(userWallet==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("无数据");
            return resp;
        }
        UserWalletVo userVo = TransUtil.transEntity(userWallet,UserWalletVo.class);
        resp.setData(userVo);
        return resp;
    }
}
