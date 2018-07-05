package com.shop.user.controller;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.base.util.TransUtil;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.rest.UserResource;
import com.shop.user.model.pojo.ShopUser;
import com.shop.user.service.UserService;
import com.util.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Api("用户接口")
@RequestMapping("/user")
@RestController
public class UserController implements UserResource {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询用户信息", notes = "查询用户信息")
    @RequestMapping(value = "/queryUserInfo", method = RequestMethod.POST)
    public Resp<UserInfo> queryUserInfo(@RequestBody Req<Void> req) {
        Resp<UserInfo> resp = new Resp<UserInfo>();
        String token = req.getHeader().getToken();
        if(StringUtils.isBlank(token)) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("用户未登录");
            return resp;
        }
        Long userId = userService.getLoginUserId(token);
        if (userId == null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("登录已过期");
            return resp;
        }
        UserInfo userInfo = userService.getUserInfo(userId);
        resp.setData(userInfo);
        return resp;
    }

    @ApiOperation(value = "根据手机号查询用户", notes = "根据手机号查询用户")
    @RequestMapping(value = "/queryByMobile", method = RequestMethod.POST)
    public Resp<UserInfo> queryByMobile(@RequestBody Req<String> req) {
        Resp<UserInfo> resp = new Resp<UserInfo>();
        String mobile = req.getData();
        if(StringUtils.isBlank(mobile)) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        ShopUser shopUser = userService.queryByMobile(mobile);
        if(shopUser==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("无数据");
            return resp;
        }
        UserInfo userInfo = TransUtil.transEntity(shopUser,UserInfo.class);
        //注册时间
        Date regTime = userInfo.getRegTime();
        String regTimeStr=transDateTimeStr(regTime);
        userInfo.setRegTimeStr(regTimeStr);

        //最后登陆时间
        Date lastLoginTime = userInfo.getLastLoginTime();
        String lastLoginTimeStr=transDateTimeStr(lastLoginTime);
        userInfo.setLastLoginTimeStr(lastLoginTimeStr);
        resp.setData(userInfo);
        return resp;
    }

    @ApiOperation(value = "根据id查询用户", notes = "根据id查询用户")
    @RequestMapping(value = "/queryUserById", method = RequestMethod.POST)
    public Resp<UserInfo> queryUserById(@RequestBody Req<Long> req) {
        Resp<UserInfo> resp = new Resp<UserInfo>();
        Long userId = req.getData();
        if(userId==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        UserInfo userInfo  = userService.getUserInfo(userId);
        if(userInfo==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("无数据");
            return resp;
        }
        resp.setData(userInfo);
        return resp;
    }

    private String transDateTimeStr(Date date) {
        if(date==null) {
            return "";
        }
        return DateUtil.formatDate(date,DateUtil.FMT_YMD_HMS);
    }


}
