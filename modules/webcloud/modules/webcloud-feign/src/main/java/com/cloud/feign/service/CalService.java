package com.cloud.feign.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "webcloud-appa", fallback = CalServiceHystric.class)
public interface CalService {

    //FeignClient感觉没启作用，也不知道是什么原因
    @RequestMapping(value = "web/add" ,method = RequestMethod.GET)
    public Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b);
}
