package com.shop.order.controller;

import com.shop.order.api.model.OrderInfo;
import com.shop.order.api.model.OrderVo;
import com.shop.order.api.model.base.Rep;
import com.shop.order.api.model.base.RepHeader;
import com.shop.order.api.model.base.Req;
import com.shop.order.api.model.base.ReqHeader;
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
    public Rep<OrderInfo> createOrder(@RequestBody Req<OrderVo> req, Errors errors) {
        OrderVo orderVo = req.getData();
        Long userId=orderVo==null?null:orderVo.getUserId();
        logger.info("OrderController createOrder start,orderVo={}",orderVo);

        Rep<OrderInfo> rep = new Rep<OrderInfo>();
        boolean checkPass=checkOrder(orderVo);
        if(!checkPass) {
            rep.getHeader().setRt(RepHeader.FAIL);
            rep.getHeader().setMsg("参数校验失败");
            return rep;
        }
        try{
            OrderInfo orderInfo = orderService.createOrder(orderVo);
            rep.getHeader().setRt(RepHeader.SUCCESS);
            rep.setData(orderInfo);
            return  rep;
        } catch (Exception e) {
            logger.error("OrderController createOrder error,userId={}",userId,e);
            rep.getHeader().setRt(RepHeader.FAIL);
            rep.getHeader().setMsg("服务处理失败");
            return rep;
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
    public Rep<OrderInfo> getOrderInfo2(@RequestParam("orderId") Long orderId) {
//        boolean aa=true;
//        if(aa) {
//            return null;
//        }
        Rep<OrderInfo> rep= new Rep<OrderInfo>();
        if(orderId==null) {
            rep.getHeader().setRt(RepHeader.FAIL);
            return rep;
        }
        OrderInfo orderInfo = orderService.getOrderInfo2(orderId);
        rep.setData(orderInfo);
        rep.getHeader().setRt(RepHeader.SUCCESS);
        return rep;
    }

    @ApiOperation(value = "取消订单", notes = "取消订单")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public Rep<Integer> cancelOrder(@RequestParam("orderId") Long orderId) {
        logger.info("OrderController cancelOrder start,orderId={}",orderId);
        Rep<Integer> rep= new Rep<Integer>();
        if(orderId==null) {
            rep.getHeader().setRt(RepHeader.FAIL);
            return rep;
        }
        try{
            rep= orderExtendService.cancelOrder(orderId);
            return  rep;
        } catch (Exception e) {
            logger.error("OrderController cancelOrder error,orderId={}",orderId,e);
            rep.getHeader().setRt(RepHeader.FAIL);
            rep.getHeader().setMsg("服务处理失败");
            return rep;
        }
    }
}
