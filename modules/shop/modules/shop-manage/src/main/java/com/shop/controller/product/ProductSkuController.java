package com.shop.controller.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 产品sku访问类
 */
@Controller
@RequestMapping(value = "/shop/product")
public class ProductSkuController {

    private static final String PREFIX = "/shop/product/";

    //产品sku库存页面
    @RequestMapping(value = "/productSkuView")
    public String productSkuView()
    {
        return PREFIX + "productSkuView";
    }

}
