package com.shop.controller.shop;

import com.alibaba.fastjson.JSONObject;
import com.shop.controller.BaseController;
import com.shop.model.pojo.ShDiscount;
import com.shop.model.vo.ShopDiscountQueryVo;
import com.shop.service.shop.ShopDiscountService;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 商店折扣controller类
 */
@Controller
@RequestMapping(value = "/shop/shop")
public class ShopDiscountController extends BaseController {

    private static final String PREFIX = "/shop/shop/";

    @Autowired
    private ShopDiscountService shopDiscountService;

    //商店折扣页面
    @RequestMapping(value = "/shopDiscount")
    public String shopDiscount()
    {
        return PREFIX + "shopDiscount";
    }

    //商店折扣列表
    @RequestMapping(value = "/shopDiscountList")
    public void shopDiscountList(HttpServletResponse response,ShopDiscountQueryVo queryVo) {

        queryVo.genDateParam();
        queryVo.genPage();

        List<ShDiscount> list = shopDiscountService.queryPage(queryVo);
        int count = shopDiscountService.count(queryVo);

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
