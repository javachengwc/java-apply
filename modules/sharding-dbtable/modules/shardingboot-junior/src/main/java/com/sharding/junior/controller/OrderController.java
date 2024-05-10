package com.sharding.junior.controller;

import com.model.base.Resp;
import com.sharding.junior.model.entity.Order;
import com.sharding.junior.model.vo.OrderVo;
import com.sharding.junior.service.OrderService;
import com.tool.util.TransUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/order")
//@Api(value = "订单接口")
@RestController
public class OrderController {

    private static Logger logger= LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderService orderService;

    //@ApiOperation(value = "创建订单", notes = "创建订单")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public Resp<OrderVo> createOrder(OrderVo orderVo) {
        logger.info("OrderController createOrder start,.................");
        Order order =orderService.createOrder(orderVo);
        OrderVo rtOrder = TransUtil.transEntity(order,OrderVo.class);
        return Resp.data(rtOrder);
    }

    @RequestMapping(value = "/queryOrder", method = RequestMethod.GET)
    public Resp<OrderVo> queryOrder(@RequestParam(value = "orderNo") String orderNo) {
        logger.info("OrderController queryOrder start, orderNo={}",orderNo);
        Order order =orderService.queryByOrderNo(orderNo);
        if(order==null) {
            return Resp.error("查无结果");
        }
        OrderVo orderVo = TransUtil.transEntity(order,OrderVo.class);
        return Resp.data(orderVo);
    }

}
