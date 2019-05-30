package com.micro.order.service.impl;

import com.micro.order.dao.mapper.ShopOrderMapper;
import com.micro.order.enums.OrderStatuEnum;
import com.micro.order.model.pojo.ShopOrder;
import com.micro.order.model.third.GoodsStockReq;
import com.micro.order.model.third.UserWalletReq;
import com.micro.order.service.OrderService;
import com.micro.order.service.PaymentService;
import com.micro.order.service.remote.GoodsService;
import com.micro.order.service.remote.UserWalletService;
import com.shop.base.model.Req;
import com.shop.base.model.ReqHeader;
import com.shop.base.model.Resp;
import org.dromara.hmily.annotation.Hmily;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private OrderService orderService;

    //tcc中的try
    @Hmily(confirmMethod = "confirmOrder", cancelMethod = "failOrder")
    public boolean pay(ShopOrder order) {
        Long orderId = order.getId();
        Long goodsId = order.getGoodsId();
        Integer goodsCount = order.getGoodsCount();
        Long buyerId = order.getBuyerId();
        logger.info("PaymentServiceImpl pay start,orderId={},threadId={}",orderId,Thread.currentThread().getId());

        int preStatu = order.getStatu();
        order.setStatu(OrderStatuEnum.PAYING.getValue());
        orderService.changeStatu(orderId,preStatu,order.getStatu());
        logger.info("PaymentServiceImpl pay 改变订单状态为支付中,orderId={}",orderId);

        GoodsStockReq goodsStockReq = new GoodsStockReq(goodsId,goodsCount);
        Req<GoodsStockReq> decreaseReq = new Req<GoodsStockReq>();
        decreaseReq.setHeader(new ReqHeader());
        decreaseReq.setData(goodsStockReq);
        logger.info("PaymentServiceImpl pay 开始扣减库存,goodsId={},goodsCount={}",goodsId,goodsCount);
        Resp<Void>  decreaseResp=goodsService.decreaseStock(decreaseReq);
        logger.info("PaymentServiceImpl pay 调用扣减库存,返回结果:{},goodsId={}",decreaseResp,goodsId);

        UserWalletReq userWalletReq= new UserWalletReq(buyerId,order.getTotalAmount());
        Req<UserWalletReq> walletReq = new Req<UserWalletReq>();
        walletReq.setHeader(new ReqHeader());
        walletReq.setData(userWalletReq);
        logger.info("PaymentServiceImpl pay 开始扣减用户钱包金额,userId={},amount={}",buyerId,order.getTotalAmount());
        Resp<Void> walletResp=userWalletService.decreaseAmount(walletReq);
        logger.info("PaymentServiceImpl pay 调用扣减用户钱包金额,返回结果:{},userId={}",walletResp,buyerId);

        return true;

    }

    //tcc中的confirm
    public boolean confirmOrder(ShopOrder order) {
        Long orderId = order.getId();
        logger.info("PaymentServiceImpl confirmOrder start,orderId={},threadId={}",orderId,Thread.currentThread().getId());

        int preStatu = order.getStatu();
        order.setStatu(OrderStatuEnum.COMPLETE.getValue());
        orderService.changeStatu(orderId,preStatu,order.getStatu());
        logger.info("PaymentServiceImpl confirmOrder 改变订单状态为完成,orderId={}",orderId);
        return true;
    }

    //tcc中的cancel
    public boolean failOrder(ShopOrder order) {
        Long orderId = order.getId();
        logger.info("PaymentServiceImpl failOrder start,orderId={},threadId={}",orderId,Thread.currentThread().getId());

        int preStatu = order.getStatu();
        order.setStatu(OrderStatuEnum.PAY_FAIL.getValue());
        orderService.changeStatu(orderId,preStatu,order.getStatu());
        logger.info("PaymentServiceImpl confirmOrder 改变订单状态为支付失败,orderId={}",orderId);
        return true;
    }

}
