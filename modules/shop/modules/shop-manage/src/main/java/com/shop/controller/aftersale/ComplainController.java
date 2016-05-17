package com.shop.controller.aftersale;

import com.shop.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 维权controller类
 */
@Controller
@RequestMapping(value = "/shop/complain")
public class ComplainController extends BaseController {

    private static final String PREFIX = "/shop/complain/";

    //维权页面
    @RequestMapping(value = "/complain")
    public String complain()
    {
        return PREFIX + "complain";
    }

}