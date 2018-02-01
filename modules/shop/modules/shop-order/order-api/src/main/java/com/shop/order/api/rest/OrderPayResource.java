package com.shop.order.api.rest;

import com.shop.order.api.model.IcbcPayVo;
import com.shop.order.api.model.OrderPayVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Api(value = "订单支付接口")
@RequestMapping(value="/order/pay")
public interface OrderPayResource {

    @ApiOperation(value = "创建支付宝下单", notes = "创建支付宝下单")
    @RequestMapping(value = "/aliPayCreate", method = RequestMethod.POST)
    public OrderPayVo aliPayCreate(@RequestBody OrderPayVo orderPayVo, HttpServletRequest request);

    @ApiOperation(value = "创建微信下单", notes = "创建微信下单")
    @RequestMapping(value = "/wxPayCreate", method = RequestMethod.POST)
    public OrderPayVo wxPayCreate(@RequestBody OrderPayVo orderPayVo, HttpServletRequest request);

    @ApiOperation(value = "工行e支付下单", notes = "工行e支付下单")
    @RequestMapping(value = "/icbcPayCreate", method = RequestMethod.POST)
    public OrderPayVo icbcPayCreate(@RequestBody OrderPayVo orderPayVo, HttpServletRequest request);

    @ApiOperation(value = "工行e支付支付", notes = "工行e支付支付")
    @RequestMapping(value = "/icbcPay", method = RequestMethod.POST)
    public IcbcPayVo icbcPay(@RequestBody IcbcPayVo icbcPayVo, HttpServletRequest request);

    @RequestMapping(value = "/aliPayCallback", method = RequestMethod.POST)
    @ApiOperation(value = "支付宝回调接口", notes = "支付宝回调接口")
    public String aliPayCallBack(HttpServletRequest request);

    @RequestMapping(value = "/wxPayCallback", method = RequestMethod.POST)
    @ApiOperation(value = "微信支付回调接口", notes = "微信支付回调接口")
    public String wxPayCallback(HttpServletRequest request);

    @RequestMapping(value = "/icbcPayCallback", method = RequestMethod.POST)
    @ApiOperation(value = "工行e支付回调接口", notes = "工行e支付回调接口")
    public String icbcPayCallback(HttpServletRequest request);
}
