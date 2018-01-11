package com.shop.order.controller;

import com.shop.order.api.model.OrderInfo;
import com.shop.order.api.model.base.Rep;
import com.shop.order.api.rest.OrderResCtrl;
import com.shop.order.api.rest.OrderResource;
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
public class OrderController implements OrderResource,OrderResCtrl {

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

    @ApiOperation(value = "获取订单信息", notes = "获取订单信息")
    @RequestMapping(value="/getOrderInfo2",method= RequestMethod.GET)
    public Rep<OrderInfo> getOrderInfo2(@RequestParam("orderId") Long orderId) {
//        boolean aa=true;
//        if(aa) {
//            return null;
//        }
        Rep<OrderInfo> rep= new Rep<OrderInfo>();
        if(orderId==null) {
            rep.getHeader().setRet("F");
            return rep;
        }
        OrderInfo orderInfo = orderService.getOrderInfo2(orderId);
        rep.setData(orderInfo);
        rep.getHeader().setRet("S");
        return rep;
    }
}
