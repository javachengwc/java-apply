package com.shop.book.controller;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.book.service.manager.CacheManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cache")
@Api(value = "缓存接口")
public class CacheController {

    @ApiOperation(value = "获取所有缓存key", notes = "获取所有缓存key")
    @RequestMapping(value = "/getAllCacheKey", method = RequestMethod.GET)
    public Resp<Map<String,Long>> getAllCacheKey(){
        Map<String,Long> map = CacheManager.getAllCache();
        Resp<Map<String,Long>> resp = Resp.success(map);
        return resp;
    }

    @ApiOperation(value = "清楚缓存key", notes = "清楚缓存key,不传参表示清除所有缓存")
    @RequestMapping(value = "/cleanCache", method = RequestMethod.POST)
    public Resp<Void> cleanCache(@RequestBody Req<String> req){
        String cacheKey = req.getData();
        CacheManager.cleanCache(cacheKey);
        Resp<Void> resps = new Resp<Void>();
        return resps;
    }
}
