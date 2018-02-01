package com.shop.order.controller;

import com.shop.order.api.model.IcbcPayVo;
import com.shop.order.api.model.OrderPayVo;
import com.shop.order.api.rest.OrderPayResource;
import com.shop.order.service.OrderIcbcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderIcbcService orderIcbcService;

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

    @ApiOperation(value = "工行e支付下单", notes = "工行e支付下单")
    @RequestMapping(value = "/icbcPayCreate", method = RequestMethod.POST)
    public OrderPayVo icbcPayCreate(@RequestBody OrderPayVo orderPayVo, HttpServletRequest request) {
        return null;
    }

    @ApiOperation(value = "工行e支付支付", notes = "工行e支付支付")
    @RequestMapping(value = "/icbcPay", method = RequestMethod.POST)
    public IcbcPayVo icbcPay(@RequestBody IcbcPayVo icbcPayVo, HttpServletRequest request) {
        logger.info("OrderPayController icbcPay start ,icbcPayVo={}",icbcPayVo);
        IcbcPayVo payVo=orderIcbcService.icbcPay(icbcPayVo);
        return payVo;
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

    @RequestMapping(value = "/icbcPayCallback", method = RequestMethod.POST)
    @ApiOperation(value = "工行e支付回调接口", notes = "工行e支付回调接口")
    public String icbcPayCallback(HttpServletRequest request) {
        return null;
    }
}
