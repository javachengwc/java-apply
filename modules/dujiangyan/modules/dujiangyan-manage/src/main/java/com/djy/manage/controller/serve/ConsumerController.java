package com.djy.manage.controller.serve;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 消费者维度服务controller类
 */
@Controller
@RequestMapping(value = "/djy/serve")
public class ConsumerController {

    private static final String PREFIX = "/dujiangyan/serve/";

    @RequestMapping(value = "/consumer")
    public String consumer() {
        return  PREFIX+"consumer";
    }
}
