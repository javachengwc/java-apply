package com.shop.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.shop.controller.BaseController;
import com.shop.model.pojo.OdOrder;
import com.shop.model.vo.OrderQueryVo;
import com.shop.service.order.OrderService;
import com.util.web.HttpRenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 订单controller类
 */
@Controller
@RequestMapping(value = "/shop/order")
public class OrderController extends BaseController {

    private static final String PREFIX = "/shop/order/";

    @Autowired
    private OrderService orderService;

    //产品页面
    @RequestMapping(value = "/order")
    public String order()
    {
        return PREFIX + "order";
    }

    //产品列表
    @RequestMapping(value = "/orderList")
    public void orderList(HttpServletResponse response,OrderQueryVo queryVo) {

        queryVo.genDateParam();
        queryVo.genPage();

        List<OdOrder> list = orderService.queryPage(queryVo);
        int count = orderService.count(queryVo);

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
