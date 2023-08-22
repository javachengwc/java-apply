package com.mybatis.controller;

import com.model.base.Resp;
import com.mybatis.model.entity.Data;
import com.mybatis.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @RequestMapping("/queryList")
    public Resp<List<Data>> queryList() {
        List<Data> list =dataService.queryList();
        return Resp.data(list);
    }

}
