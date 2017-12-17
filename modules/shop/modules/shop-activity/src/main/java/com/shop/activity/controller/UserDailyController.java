package com.shop.activity.controller;

import com.shop.activity.model.UserDailyInfo;
import com.shop.activity.service.UserDailyService;
import com.util.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Api(value = "用户每天数据接口")
@RequestMapping("/userDaily")
public class UserDailyController {

    @Autowired
    private UserDailyService userDailyService;

    @ApiOperation(value = "获取用户每天数据", notes = "获取用户每天数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "Long", paramType = "query"),
        @ApiImplicitParam(name = "day", value = "日期 yyyy-MM-dd", required = true, dataType = "String", paramType = "query")
    })
    @RequestMapping(value="/queryUserDaily",method= RequestMethod.GET)
    public UserDailyInfo queryUserDaily(@RequestParam("userId") Long userId,@RequestParam("day") String day){
        if(userId==null || !DateUtil.checkDate(day, DateUtil.FMT_YMD)) {
            return null;
        }
        Date date =DateUtil.getDate(day,DateUtil.FMT_YMD);
        UserDailyInfo userDailyInfo= userDailyService.queryUserDaily(userId, date);
        return userDailyInfo;
    }
}
