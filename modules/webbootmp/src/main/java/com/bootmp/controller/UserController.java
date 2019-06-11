package com.bootmp.controller;

import com.bootmp.model.pojo.User;
import com.bootmp.model.vo.UserVo;
import com.bootmp.service.UserService;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("用户接口")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "根据id查询用户", notes = "根据id查询用户")
    @RequestMapping(value = "/queryUserById", method = RequestMethod.POST)
    public Resp<UserVo> queryUserById(@RequestBody Req<Long> req) {
        Resp<UserVo> resp = new Resp<UserVo>();
        Long userId = req.getData();
        if(userId==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        User user  = userService.getById(userId);
        if(user==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("无数据");
            return resp;
        }
        UserVo userVo =new UserVo();
        BeanUtils.copyProperties(user, userVo);
        resp.setData(userVo);
        return resp;
    }
}
