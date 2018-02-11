package com.shop.order.service.impl;

import com.shop.order.api.enums.OrderActionEnum;
import com.shop.order.api.enums.OrderStatuEnum;
import com.shop.order.api.model.base.Rep;
import com.shop.order.api.model.base.RepHeader;
import com.shop.order.dao.mapper.ShopOrderMapper;
import com.shop.order.model.pojo.OrderOperateRecord;
import com.shop.order.model.pojo.ShopOrder;
import com.shop.order.model.pojo.ShopOrderExample;
import com.shop.order.service.OrderExtendService;
import com.shop.order.service.OrderService;
import com.shop.order.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderExtendServiceImpl implements OrderExtendService {

    private static Logger logger= LoggerFactory.getLogger(OrderExtendServiceImpl.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShopOrderMapper shopOrderMapper;

    private volatile OrderExtendService txOrderExtendService;

    public OrderExtendService gainTxOrderExtendService() {
        if(txOrderExtendService==null) {
            txOrderExtendService= SpringContextUtils.getBean(OrderExtendService.class);
        }
        return txOrderExtendService;
    }

    //取消订单
    public Rep<Integer> cancelOrder(Long orderId) {
        logger.info("OrderExtendServiceImpl cancelOrder start,orderId={}",orderId);
        Rep<Integer> rep =new Rep<Integer>();
        ShopOrder shopOrder =orderService.getById(orderId);
        if(shopOrder==null) {
            rep.getHeader().setRt(RepHeader.FAIL);
            return rep;
        }
        int statu = shopOrder.getStatu();
        if(statu != OrderStatuEnum.INIT.getValue() &&
                statu !=OrderStatuEnum.PENDING_PAY.getValue() &&
                statu !=OrderStatuEnum.PENDING_DELIVER.getValue()) {
            rep.getHeader().setRt(RepHeader.FAIL);
            return rep;
        }
        boolean rt =gainTxOrderExtendService().innerCancelOrder(shopOrder);
        if(rt) {
            rep.getHeader().setRt(RepHeader.SUCCESS);
        } else {
            //处理失败
            rep.getHeader().setRt(RepHeader.FAIL);
        }
        return rep;
    }

    @Transactional
    public Boolean innerCancelOrder(ShopOrder shopOrder) {
        Long orderId = shopOrder.getId();
        logger.info("OrderExtendServiceImpl innerCancelOrder start ,orderId={}",orderId);
        int curStatu = shopOrder.getStatu();
        int nextStatu =OrderStatuEnum.CLOSE.getValue();
        Integer rt =uptOrderCancel(shopOrder,curStatu,nextStatu);
        logger.info("OrderExtendServiceImpl innerCancelOrder uptOrderCancel ," +
                "orderId={},rt={}",orderId,rt);
        if(rt>0) {
            //记录订单取消操作
            OrderOperateRecord orderOperateRecord=recordOrderCancelOperate(shopOrder,curStatu,nextStatu);
            //记录状态变更流水
            orderService.recordOrderStatuChange(orderOperateRecord);
            return true;
        } else {
            shopOrder = orderService.getById(orderId);
            if(shopOrder.getStatu()==nextStatu) {
                //被其他进程成功取消订单
                return true;
            }
        }
        return false;
    }

    public Integer uptOrderCancel(ShopOrder shopOrder,int curStatu,int nextStatu) {
        Date now = new Date();
        Long orderId = shopOrder.getId();
        ShopOrder uptOrder = new ShopOrder();
        uptOrder.setId(orderId);
        uptOrder.setIsCancel(1);
        uptOrder.setStatu(nextStatu);
        uptOrder.setModifiedTime(now);
        uptOrder.setCancelTime(now);

        ShopOrderExample example = new ShopOrderExample();
        ShopOrderExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(orderId);
        criteria.andStatuEqualTo(curStatu);
        Integer rt =shopOrderMapper.updateByExampleSelective(uptOrder,example);
        return rt;
    }

    //记录订单取消操作
    public OrderOperateRecord recordOrderCancelOperate(ShopOrder shopOrder,
                                                       int curStatu,int nextStatu) {
        OrderOperateRecord operateRecord = new OrderOperateRecord();
        Date now = new Date();
        operateRecord.setCreateTime(now);
        operateRecord.setModifiedTime(now);
        operateRecord.setOrderNo(shopOrder.getOrderNo());
        operateRecord.setPreStatu(curStatu);
        operateRecord.setCurStatu(nextStatu);
        operateRecord.setOperateAction(OrderActionEnum.CANCEl.getValue());
        operateRecord.setOperateDesc(OrderActionEnum.CANCEl.getName());
        operateRecord.setOperatorId(null);
        operateRecord.setOperatorName("");
        Integer rt =orderService.addOrderOperate(operateRecord);
        return operateRecord;
    }

}
