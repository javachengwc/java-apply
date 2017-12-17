package com.shop.order.controller;

import com.shop.order.model.OrderInfo;
import com.shop.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "订单接口")
@RestController
@RequestMapping(value="/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "获取订单信息", notes = "获取订单信息")
    @RequestMapping(value="/getOrderInfo",method= RequestMethod.GET)
    public OrderInfo getOrderInfo(@RequestParam("orderId") Long orderId){
        if(orderId==null) {
            return null;
        }
        return orderService.getOrderInfo(orderId);
    }
}
