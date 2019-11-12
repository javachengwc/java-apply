package com.micro.user.controller;

import com.micro.user.model.pojo.User;
import com.micro.user.model.vo.UserVo;
import com.micro.user.service.UserService;
import com.model.base.Req;
import com.model.base.Resp;
import com.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value="用户信息接口",description="用户信息接口")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "根据id查询用户", notes = "根据id查询用户")
    @RequestMapping(value = "/queryUserById", method = RequestMethod.POST)
    public Resp<UserVo> queryUserById(@RequestBody Req<Long> req) {
        Long userId = req.getData();
        if(userId==null) {
            return Resp.error("参数错误");
        }
        User user  = userService.getById(userId);
        if(user==null) {
            return Resp.error("无用户信息");
        }
        UserVo userVo = TransUtil.transEntity(user,UserVo.class);
        return Resp.data(userVo);
    }
}
