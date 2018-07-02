package com.shop.order.api.rest;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.order.api.model.OrderInfo;
import com.shop.order.api.model.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/order")
@Api(value = "订单相关接口")
public interface OrderResCtrl {

    @ApiOperation(value = "创建订单", notes = "创建订单")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public Resp<OrderInfo> createOrder(@RequestBody Req<OrderVo> req, Errors errors);

    @ApiOperation(value = "获取订单信息", notes = "获取订单信息")
    @RequestMapping(value="/getOrderInfo",method= RequestMethod.GET)
    public OrderInfo getOrderInfo(@RequestParam("orderId") Long orderId);

    @ApiOperation(value = "获取订单信息2", notes = "获取订单信息2")
    @RequestMapping(value="/getOrderInfo2",method= RequestMethod.GET)
    public Resp<OrderInfo> getOrderInfo2(@RequestParam("orderId") Long orderId);

    @ApiOperation(value = "取消订单", notes = "取消订单")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public Resp<Integer> cancelOrder(@RequestParam("orderId") Long orderId);
}
