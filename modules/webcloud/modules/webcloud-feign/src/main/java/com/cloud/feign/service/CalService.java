package com.cloud.feign.service;

import com.cloud.feign.config.FeignConfig;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "webcloud-appa",configuration = FeignConfig.class,fallback=CalServiceHystric.class)
public interface CalService {

    @ResponseBody
    @RequestMapping(value = "/web/add" ,method = RequestMethod.GET)
    public Integer add(@RequestParam(value = "a") Integer a, @RequestParam(value = "b") Integer b);
}
