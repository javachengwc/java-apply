package com.shop.controller.aftersale;

import com.shop.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 售后controller类
 */
@Controller
@RequestMapping(value = "/shop/aftersale")
public class AftersaleController extends BaseController {

    private static final String PREFIX = "/shop/aftersale/";

    //售后页面
    @RequestMapping(value = "/aftersale")
    public String aftersale()
    {
        return PREFIX + "aftersale";
    }

}
