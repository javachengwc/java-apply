package com.sharding.junior.controller;

import com.model.base.Resp;
import com.sharding.junior.model.entity.User;
import com.sharding.junior.model.vo.UserVo;
import com.sharding.junior.service.UserService;
import com.tool.util.TransUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    private static Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserService userService;

    @RequestMapping(value = "/addUser", method = {RequestMethod.GET,RequestMethod.POST})
    public Resp<UserVo> addUser(UserVo userVo) {
        logger.info("UserController addUser start,.................");
        User user =userService.addUser(userVo);
        UserVo rtUser = TransUtil.transEntity(user,UserVo.class);
        return Resp.data(rtUser);
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public Resp<UserVo> getUser(@RequestParam(value = "userId") Long userId) {
        logger.info("UserController getUser start, userId={}",userId);
        User user =userService.getById(userId);
        if(user==null) {
            return Resp.error("查无结果");
        }
        UserVo rtUser = TransUtil.transEntity(user,UserVo.class);
        return Resp.data(rtUser);
    }

}
