package com.mybatis.controller;

import com.model.base.Resp;
import com.mybatis.model.entity.CacheTable;
import com.mybatis.service.CacheTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/cacheTable")
public class CacheTableController {

    @Autowired
    private CacheTableService cacheTableService;

    @RequestMapping("/queryList")
    public Resp<List<CacheTable>> queryList() {
        List<CacheTable> list =cacheTableService.queryList();
        ExecutorService executorService =Executors.newFixedThreadPool(5);
        for(int i=0;i<5;i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        cacheTableService.queryList();
                    }
                }
            });
        }
        return Resp.data(list);
    }
}
