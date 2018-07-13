package com.sharding.bootdemo.controller;

import com.sharding.bootdemo.model.vo.OrderItemVo;
import com.sharding.bootdemo.model.vo.OrderQueryVo;
import com.sharding.bootdemo.model.vo.OrderVo;
import com.sharding.bootdemo.service.OrderService;
import com.util.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RequestMapping("/order")
@Api(value = "订单接口")
@RestController
public class OrderController {

    private static Logger logger= LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderService orderService;

    @ApiOperation(value = "创建订单", notes = "创建订单")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public OrderVo createOrder(@RequestBody OrderVo orderVo) {
        logger.info("OrderController createOrder start,.................");
        OrderVo order =orderService.createOrder(orderVo);
        return  order;
    }

    @ApiOperation(value = "订单分页查询", notes = "订单分页查询")
    @RequestMapping(value = "/queryOrderPage", method = RequestMethod.POST)
    public Page<OrderVo> queryOrderPage(@RequestBody OrderQueryVo queryVo) {
        logger.info("OrderController queryOrderPage start,.................");
        Page<OrderVo> page =orderService.queryPage(queryVo);
        return  page;
    }

    @ApiOperation(value = "订单详单查询", notes = "订单详单查询")
    @RequestMapping(value = "/queryOrderItem", method = RequestMethod.GET)
    public List<OrderItemVo> queryOrderItem(Long orderId) {
        logger.info("OrderController queryOrderItem start,orderId={}",orderId);
        if(orderId==null) {
            return Collections.emptyList();
        }
        List<OrderItemVo> list =orderService.queryOrderItem(orderId);
        return  list;
    }
}
