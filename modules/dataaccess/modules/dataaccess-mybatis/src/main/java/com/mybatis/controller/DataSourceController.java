package com.mybatis.controller;

import com.model.base.Resp;
import com.mybatis.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @RequestMapping("/queryDataSourceInfo")
    public Resp<Object> queryDataSourceInfo() {
        Object info =dataSourceService.queryDataSourceInfo();
        return Resp.data(info);
    }
}
