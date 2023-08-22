package com.mybatis.controller;

import com.model.base.Resp;
import com.mybatis.service.MybatisDirectAccessDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mybatis")
public class MybatisController {

    @Autowired
    private MybatisDirectAccessDBService mybatisDirectAccessDBService;

    @ResponseBody
    @RequestMapping("/initData")
    public Resp<Void> initData() {
        mybatisDirectAccessDBService.initDataWithDirectAccess();
        return Resp.success();
    }

}
