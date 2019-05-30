package com.micro.order.controller;

import com.micro.order.model.pojo.ShopOrder;
import com.micro.order.model.vo.OrderVo;
import com.micro.order.service.OrderService;
import com.shop.base.model.Req;
import com.shop.base.model.Resp;
import com.shop.base.model.RespHeader;
import com.shop.base.util.TransUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api("订单接口")
@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "根据id查询订单", notes = "根据id查询订单")
    @RequestMapping(value = "/queryOrderById", method = RequestMethod.POST)
    public Resp<OrderVo> queryOrderById(@RequestBody Req<Long> req) {
        Resp<OrderVo> resp = new Resp<OrderVo>();
        Long orderId = req.getData();
        if(orderId==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("参数校验错误");
            return resp;
        }
        ShopOrder order  = orderService.getById(orderId);
        if(order==null) {
            resp.getHeader().setCode(RespHeader.FAIL);
            resp.getHeader().setMsg("无数据");
            return resp;
        }
        OrderVo orderVo = TransUtil.transEntity(order,OrderVo.class);
        resp.setData(orderVo);
        return resp;
    }
}
