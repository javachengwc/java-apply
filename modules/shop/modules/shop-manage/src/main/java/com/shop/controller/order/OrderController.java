package com.shop.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.shop.controller.BaseController;
import com.shop.model.pojo.OdOrder;
import com.shop.model.pojo.ShopOrder;
import com.shop.model.vo.OrderQueryVo;
import com.shop.service.order.OrderService;
import com.util.web.HttpRenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger= LoggerFactory.getLogger(OrderController.class);

    private static final String PREFIX = "/shop/order/";

    @Autowired
    private OrderService orderService;

    //老订单页面
    @RequestMapping(value = "/order")
    public String order()
    {
        return PREFIX + "order";
    }

    //老订单列表
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

    //订单页面
    @RequestMapping(value = "/shopOrder")
    public String shopOrder()
    {
        return PREFIX + "shopOrder";
    }

    @RequestMapping(value = "/queryOrderPage")
    public void queryOrderPage(HttpServletResponse response,OrderQueryVo queryVo) {
        logger.info("OrderController queryOrderPage start........");
        queryVo.genDateParam();
        queryVo.genPage();

        List<ShopOrder> list = orderService.queryOrderPage(queryVo);
        int count = orderService.orderCount(queryVo);

        JSONObject map = new JSONObject();
        map.put(DATAGRID_ROWS,list);
        map.put(DATAGRID_TOTAL,count);
        HttpRenderUtil.renderJSON(map.toJSONString(), response);
    }
}
