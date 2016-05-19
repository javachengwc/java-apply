package com.shop.controller.aftersale;

import com.shop.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 维权controller类
 */
@Controller
@RequestMapping(value = "/shop/safeguard")
public class SafeguardRightController  extends BaseController {

    private static final String PREFIX = "/shop/safeguard/";

    //维权页面
    @RequestMapping(value = "/safeguard")
    public String safeguard()
    {
        return PREFIX + "safeguard";
    }

}