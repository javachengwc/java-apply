package com.shop.order.api.rest;

import com.shop.base.model.Resp;
import com.shop.order.api.model.OrderInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Api(value = "订单接口")
@Path("/order")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrderResource {

    @ApiOperation(value = "获取订单信息", notes = "获取订单信息")
    @GET
    @Path("/getOrderInfo")
    public OrderInfo getOrderInfo(@QueryParam("orderId") Long orderId);

    @ApiOperation(value = "获取订单信息2", notes = "获取订单信息2")
    @GET
    @Path("/getOrderInfo2")
    public Resp<OrderInfo> getOrderInfo2(@QueryParam("orderId") Long orderId);

}
