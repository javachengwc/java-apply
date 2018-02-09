package com.shop.order.service.impl;

import com.shop.order.api.enums.*;
import com.shop.order.api.model.OrderInfo;
import com.shop.order.api.model.OrderVo;
import com.shop.order.dao.mapper.OrderOperateRecordMapper;
import com.shop.order.dao.mapper.OrderStatuChangeMapper;
import com.shop.order.dao.mapper.ShopOrderMapper;
import com.shop.order.model.pojo.OrderOperateRecord;
import com.shop.order.model.pojo.OrderStatuChange;
import com.shop.order.model.pojo.ShopOrder;
import com.shop.order.service.OrderService;
import com.shop.order.service.remote.UserService;
import com.shop.order.util.SpringContextUtils;
import com.shop.user.api.model.UserInfo;
import com.shop.user.api.model.base.Rep;
import com.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private static Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class);

    private static Long zeroLong =0L;

    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Autowired
    private OrderOperateRecordMapper orderOperateRecordMapper;

    @Autowired
    private OrderStatuChangeMapper orderStatuChangeMapper;

    @Autowired
    private UserService userService;

    public OrderInfo createOrder(OrderVo orderVo) {
        Long userId = orderVo.getUserId();
        logger.info("OrderServiceImpl createOrder start ,userId={}",userId);
        //生成订单编号
        String orderNo = genOrderNo(orderVo);
        orderVo.setOrderNo(orderNo);
        ShopOrder shopOrder = new ShopOrder();
        BeanUtils.copyProperties(orderVo,shopOrder);
        int statu= OrderStatuEnum.PENDING_PAY.getValue();
        shopOrder.setStatu(statu);
        //默认值填充
        fillDef(shopOrder);
        logger.info("OrderServiceImpl createOrder prepare data done,orderNo={}",orderNo);
        //记录订单相关数据
        //((OrderService) AopContext.currentProxy()).recordOrderCreate(shopOrder);
        //必须这样,service类中的非事物方法调用同类中的事物方法,
        //必须通过获取spring容器管理的此类的代理对象来调用事物方法，事物才生效
        OrderService orderService =SpringContextUtils.getBean(OrderService.class);
        orderService.recordOrderCreate(shopOrder);

        Long orderId = shopOrder.getId();
        logger.info("OrderServiceImpl createOrder end,orderId={},orderNo={}",orderId,orderNo);
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(shopOrder,orderInfo);
        return orderInfo;
    }

    public String genOrderNo(OrderVo orderVo) {
        return UUID.randomUUID().toString().substring(0,16);
    }

    public void fillDef(ShopOrder shopOrder) {
        Date now = new Date();
        shopOrder.setIsOverbuy(0);//超卖: 0,正常 1,超卖
        shopOrder.setIsCancel(0);
        shopOrder.setPayState(PayStateEnum.INIT.getValue());
        shopOrder.setPayChannel(PayChannelEnum.NIL.getValue());
        shopOrder.setIsDelive(DeliverStatuEnum.NOT_DELIVER.getValue());
        shopOrder.setIsDeliveTimeout(0); //是否发货超时 0--否 1--是
        shopOrder.setRemindDeliverCount(0);
        shopOrder.setIsReceive(ReceiveStatuEnum.NOT_RECEIVE.getValue());
        if(shopOrder.getPostage()==null) {
            //邮费
            shopOrder.setPostage(zeroLong);
        }
        if(shopOrder.getExchangeScore()==null) {
            //兑换的积分数量
            shopOrder.setExchangeScore(0);
        }
        if(shopOrder.getExchangeCash()==null) {
            //兑换的金额
            shopOrder.setExchangeCash(zeroLong);
        }
        if(shopOrder.getCouponPrice()==null) {
            //优惠券金额
            shopOrder.setCouponPrice(zeroLong);
        }
        if(shopOrder.getDiscountPrice()==null) {
            //活动优惠的价格
            shopOrder.setDiscountPrice(zeroLong);
        }
        shopOrder.setCreateTime(now);
        shopOrder.setModifiedTime(now);
        shopOrder.setModifierId(shopOrder.getUserId());
    }

    @Transactional
    public Boolean recordOrderCreate(ShopOrder shopOrder) {
        boolean throwFlag=true;
        String orderNo = shopOrder.getOrderNo();
        logger.info("OrderServiceImpl recordOrderCreate start,orderNo={}",orderNo);
        Integer rt =addOrder(shopOrder);
        logger.info("OrderServiceImpl recordOrderCreate addOrder rt={},orderNo={}",rt,orderNo);
        OrderOperateRecord operateRecord=recordOrderCreateOperate(shopOrder);
        if(throwFlag) {
            throw new RuntimeException("haha");
        }
        recordOrderStatuChange(operateRecord);
        logger.info("OrderServiceImpl recordOrderCreate end,orderNo={}",orderNo);
        return true;
    }

    //记录订单创建操作记录
    public OrderOperateRecord recordOrderCreateOperate(ShopOrder shopOrder) {
        OrderOperateRecord operateRecord = new OrderOperateRecord();
        Date createTime = shopOrder.getCreateTime();
        operateRecord.setCreateTime(createTime);
        operateRecord.setModifiedTime(createTime);
        operateRecord.setOrderNo(shopOrder.getOrderNo());
        operateRecord.setPreStatu(OrderStatuEnum.INIT.getValue());
        operateRecord.setCurStatu(shopOrder.getStatu());
        operateRecord.setOperateAction(OrderActionEnum.CREATE.getValue());
        operateRecord.setOperateDesc(OrderActionEnum.CREATE.getName());
        operateRecord.setOperatorId(shopOrder.getUserId());
        operateRecord.setOperatorName(shopOrder.getUserName());
        Integer rt =addOrderOperate(operateRecord);
        return operateRecord;
    }

    //记录订单状态变更流水
    public OrderStatuChange recordOrderStatuChange(OrderOperateRecord operateRecord) {
        OrderStatuChange statuChange = new OrderStatuChange();
        statuChange.setOrderNo(operateRecord.getOrderNo());
        Date createTime = operateRecord.getCreateTime();
        statuChange.setCreateTime(createTime);
        statuChange.setModifiedTime(createTime);
        statuChange.setCurStatu(operateRecord.getCurStatu());
        statuChange.setPreStatu(operateRecord.getPreStatu());
        statuChange.setOperateDesc(operateRecord.getOperateDesc());
        statuChange.setOperatorId(operateRecord.getOperatorId());
        statuChange.setOperatorName(operateRecord.getOperatorName());
        Integer rt =addOrderStatuChange(statuChange);
        return statuChange;
    }

    //录入订单
    public Integer addOrder(ShopOrder shopOrder) {
        Integer rt =shopOrderMapper.insert(shopOrder);
        return rt;
    }

    public Integer addOrderOperate(OrderOperateRecord orderOperateRecord) {
        Integer rt =orderOperateRecordMapper.insert(orderOperateRecord);
        return rt;
    }

    public Integer addOrderStatuChange(OrderStatuChange orderStatuChange) {
        Integer rt =orderStatuChangeMapper.insert(orderStatuChange);
        return rt;
    }

    public ShopOrder getById(Long orderId) {
        return shopOrderMapper.selectByPrimaryKey(orderId);
    }

    public OrderInfo getOrderInfo(Long orderId) {

        ShopOrder shopOrder = getById(orderId);
        if(shopOrder==null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(shopOrder,orderInfo);

        //获取用户信息
        Long userId=orderInfo.getUserId();
        try{
            logger.info("OrderServiceImpl getOrderInfo invoke getUserInfo2 begin,userId ={}",userId);
            Rep<UserInfo> rep= userService.getUserInfo2(userId);
            logger.info("OrderServiceImpl rep={}",rep);
            if(rep!=null && rep.getData()!=null) {
                orderInfo.setUserMobile(rep.getData().getMobile());
                orderInfo.setUserName(rep.getData().getName());
            }
        }catch(Exception e) {
            logger.error("OrderServiceImpl getOrderInfo invoke getUserInfo2 error,userId={},",userId,e);
        }

        //删除时间
        Date cancelTime = orderInfo.getCancelTime();
        String cancelTimeStr=transDateTimeStr(cancelTime);
        orderInfo.setCancelTimeStr(cancelTimeStr);

        //支付时间
        Date payTime = orderInfo.getPayTime();
        String payTimeStr=transDateTimeStr(payTime);
        orderInfo.setPayTimeStr(payTimeStr);

        //发货时间
        Date deliverTime = orderInfo.getDeliverTime();
        String deliverTimeStr=transDateTimeStr(deliverTime);
        orderInfo.setDeliverTimeStr(deliverTimeStr);

        //收货时间
        Date receiveTime = orderInfo.getReceiveTime();
        String receiveTimeStr=transDateTimeStr(receiveTime);
        orderInfo.setReceiveTimeStr(receiveTimeStr);

        //创建时间
        Date creatTime = orderInfo.getCreateTime();
        String creatTimeStr=transDateTimeStr(creatTime);
        orderInfo.setCreateTimeStr(creatTimeStr);

        return orderInfo;
    }

    public String transDateTimeStr(Date date) {
        if(date==null) {
            return "";
        }
        return DateUtil.formatDate(date,DateUtil.FMT_YMD_HMS);
    }

    public OrderInfo getOrderInfo2(Long orderId) {

        ShopOrder shopOrder = getById(orderId);
        if(shopOrder==null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(shopOrder,orderInfo);

        //获取用户信息
        Long userId=orderInfo.getUserId();
        try{
            logger.info("OrderServiceImpl getOrderInfo2 invoke queryUserInfo3 begin,userId ={}",userId);
            Rep<UserInfo> rep= userService.queryUserInfo3(userId);
            UserInfo userInfo =rep==null?null:rep.getData();
            logger.info("OrderServiceImpl userInfo={}",userInfo);
            if(userInfo!=null ) {
                orderInfo.setUserMobile(userInfo.getMobile());
                orderInfo.setUserName(userInfo.getName());
            }
        }catch(Exception e) {
            logger.error("OrderServiceImpl getOrderInfo2 invoke queryUserInfo3 error,userId={},",userId,e);
        }

        //删除时间
        Date cancelTime = orderInfo.getCancelTime();
        String cancelTimeStr=transDateTimeStr(cancelTime);
        orderInfo.setCancelTimeStr(cancelTimeStr);

        //支付时间
        Date payTime = orderInfo.getPayTime();
        String payTimeStr=transDateTimeStr(payTime);
        orderInfo.setPayTimeStr(payTimeStr);

        //发货时间
        Date deliverTime = orderInfo.getDeliverTime();
        String deliverTimeStr=transDateTimeStr(deliverTime);
        orderInfo.setDeliverTimeStr(deliverTimeStr);

        //收货时间
        Date receiveTime = orderInfo.getReceiveTime();
        String receiveTimeStr=transDateTimeStr(receiveTime);
        orderInfo.setReceiveTimeStr(receiveTimeStr);

        //创建时间
        Date creatTime = orderInfo.getCreateTime();
        String creatTimeStr=transDateTimeStr(creatTime);
        orderInfo.setCreateTimeStr(creatTimeStr);

        return orderInfo;

    }
}
