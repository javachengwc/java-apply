package com.djy.manage.controller.serve;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 提供者维度服务controller类
 */
@Controller
@RequestMapping(value = "/djy/serve")
public class ProviderController {

    private static final String PREFIX = "/dujiangyan/serve/";

    @RequestMapping(value = "/provider")
    public String provider() {
        return  PREFIX+"provider";
    }
}
