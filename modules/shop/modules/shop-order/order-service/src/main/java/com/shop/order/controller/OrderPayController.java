package com.shop.order.controller;

import com.shop.order.api.model.OrderPayVo;
import com.shop.order.api.rest.OrderPayResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信,支付宝的支付走的是app支付，这里只是支付前的创建下单接口,以及支付后接收结果的回调接口
 */
@RestController
@Api(value = "订单支付接口")
@RequestMapping(value="/order/pay")
public class OrderPayController implements OrderPayResource {

    private static Logger logger = LoggerFactory.getLogger(OrderPayController.class);

    @ApiOperation(value = "创建支付宝下单", notes = "创建支付宝下单")
    @RequestMapping(value = "/aliPayCreate", method = RequestMethod.POST)
    public OrderPayVo aliPayCreate(@RequestBody OrderPayVo orderPayVo, HttpServletRequest request) {
        return null;
    }

    @ApiOperation(value = "创建微信下单", notes = "创建微信下单")
    @RequestMapping(value = "/wxPayCreate", method = RequestMethod.POST)
    public OrderPayVo wxPayCreate(@RequestBody OrderPayVo orderPayVo, HttpServletRequest request) {
        return  null;
    }

    @RequestMapping(value = "/aliPayCallback", method = RequestMethod.POST)
    @ApiOperation(value = "支付宝回调接口", notes = "支付宝回调接口")
    public String aliPayCallBack(HttpServletRequest request)  {
        return  null;
    }

    @RequestMapping(value = "/wxPayCallback", method = RequestMethod.POST)
    @ApiOperation(value = "微信支付回调接口", notes = "微信支付回调接口")
    public String wxPayCallback(HttpServletRequest request) {
        return null;
    }

}
