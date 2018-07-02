package com.shop.order.controller;

import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.order.api.model.OrderInfo;
import com.shop.order.api.model.OrderVo;
import com.shop.order.api.rest.OrderResCtrl;
import com.shop.order.api.rest.OrderResource;
import com.shop.order.service.OrderExtendService;
import com.shop.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Api(value = "订单接口")
@RestController
@RequestMapping(value="/order")
public class OrderController implements OrderResource,OrderResCtrl {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderExtendService orderExtendService;

    @ApiOperation(value = "创建订单", notes = "创建订单")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public Resp<OrderInfo> createOrder(@RequestBody Req<OrderVo> req, Errors errors) {
        OrderVo orderVo = req.getData();
        Long userId=orderVo==null?null:orderVo.getUserId();
        logger.info("OrderController createOrder start,orderVo={}",orderVo);

        Resp<OrderInfo> resp = new Resp<OrderInfo>();
        boolean checkPass=checkOrder(orderVo);
        if(!checkPass) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验失败");
            return resp;
        }
        try{
            OrderInfo orderInfo = orderService.createOrder(orderVo);
            resp.getHeader().setCode(RespHeader.SUCCESS);
            resp.setData(orderInfo);
            return  resp;
        } catch (Exception e) {
            logger.error("OrderController createOrder error,userId={}",userId,e);
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("服务处理失败");
            return resp;
        }
    }

    private boolean checkOrder(OrderVo orderVo) {
        if(orderVo==null) {
            return false;
        }
        if(orderVo.getPriceAmount()==null || orderVo.getUserId()==null ||
                orderVo.getShopId()==null ) {
            return false;
        }
        return true;
    }

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
    public Resp<OrderInfo> getOrderInfo2(@RequestParam("orderId") Long orderId) {
//        boolean aa=true;
//        if(aa) {
//            return null;
//        }
        Resp<OrderInfo> resp= new Resp<OrderInfo>();
        if(orderId==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            return resp;
        }
        OrderInfo orderInfo = orderService.getOrderInfo2(orderId);
        resp.setData(orderInfo);
        resp.getHeader().setCode(RespHeader.SUCCESS);
        return resp;
    }

    @ApiOperation(value = "取消订单", notes = "取消订单")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public Resp<Integer> cancelOrder(@RequestParam("orderId") Long orderId) {
        logger.info("OrderController cancelOrder start,orderId={}",orderId);
        Resp<Integer> resp= new Resp<Integer>();
        if(orderId==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            return resp;
        }
        try{
            resp= orderExtendService.cancelOrder(orderId);
            return  resp;
        } catch (Exception e) {
            logger.error("OrderController cancelOrder error,orderId={}",orderId,e);
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("服务处理失败");
            return resp;
        }
    }
}
