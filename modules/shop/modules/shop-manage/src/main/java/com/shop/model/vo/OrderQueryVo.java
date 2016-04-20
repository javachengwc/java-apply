package com.shop.model.vo;

import com.shop.util.DateTimeUtil;
import com.util.date.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * 订单查询条件载体类
 */
public class OrderQueryVo implements Serializable{

    private String orderId;

    private Integer fromSource;

    private Integer orderState;

    private Long userId;

    private String userName;

    private Integer sellerId;

    private String sellerName;

    private Integer isOverbuy;

    private Integer isCancel;

    //支付开始时间
    private String payTimeBegin;

    //支付结束时间
    private String payTimeEnd;

    //创建开始时间
    private String createTimeBegin;

    //创建结束时间
    private String createTimeEnd;

    //发货开始时间
    private String deliverTimeBegin;

    //发货结束时间
    private String deliverTimeEnd;

    private Integer page;

    private Integer start;

    private Integer rows;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getFromSource() {
        return fromSource;
    }

    public void setFromSource(Integer fromSource) {
        this.fromSource = fromSource;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getIsOverbuy() {
        return isOverbuy;
    }

    public void setIsOverbuy(Integer isOverbuy) {
        this.isOverbuy = isOverbuy;
    }

    public Integer getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(Integer isCancel) {
        this.isCancel = isCancel;
    }

    public String getPayTimeBegin() {
        return payTimeBegin;
    }

    public void setPayTimeBegin(String payTimeBegin) {
        this.payTimeBegin = payTimeBegin;
    }

    public String getPayTimeEnd() {
        return payTimeEnd;
    }

    public void setPayTimeEnd(String payTimeEnd) {
        this.payTimeEnd = payTimeEnd;
    }

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getDeliverTimeBegin() {
        return deliverTimeBegin;
    }

    public void setDeliverTimeBegin(String deliverTimeBegin) {
        this.deliverTimeBegin = deliverTimeBegin;
    }

    public String getDeliverTimeEnd() {
        return deliverTimeEnd;
    }

    public void setDeliverTimeEnd(String deliverTimeEnd) {
        this.deliverTimeEnd = deliverTimeEnd;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public void genDateParam()
    {
        try{
            if(!StringUtils.isBlank(payTimeBegin))
            {
                payTimeBegin = DateTimeUtil.getDateTime(payTimeBegin, DateUtil.FMT_YMD).toString(DateUtil.FMT_YMD_HMS);
            }
            if(!StringUtils.isBlank(payTimeEnd))
            {
                DateTime tmpDate = DateTimeUtil.getDateTime(payTimeEnd,DateUtil.FMT_YMD);
                tmpDate= tmpDate.withTime(23,59,59,999);
                payTimeEnd = tmpDate.toString(DateUtil.FMT_YMD_HMS);
            }
            if(!StringUtils.isBlank(createTimeBegin))
            {
                createTimeBegin = DateTimeUtil.getDateTime(createTimeBegin,DateUtil.FMT_YMD).toString(DateUtil.FMT_YMD_HMS);
            }
            if(!StringUtils.isBlank(createTimeEnd))
            {
                DateTime tmpDate = DateTimeUtil.getDateTime(createTimeEnd,DateUtil.FMT_YMD);
                tmpDate= tmpDate.withTime(23,59,59,999);
                createTimeEnd = tmpDate.toString(DateUtil.FMT_YMD_HMS);
            }
            if(!StringUtils.isBlank(deliverTimeBegin))
            {
                deliverTimeBegin = DateTimeUtil.getDateTime(deliverTimeBegin,DateUtil.FMT_YMD).toString(DateUtil.FMT_YMD_HMS);
            }
            if(!StringUtils.isBlank(deliverTimeEnd))
            {
                DateTime tmpDate = DateTimeUtil.getDateTime(deliverTimeEnd,DateUtil.FMT_YMD);
                tmpDate= tmpDate.withTime(23,59,59,999);
                deliverTimeEnd = tmpDate.toString(DateUtil.FMT_YMD_HMS);
            }
        }catch(Exception e)
        {

        }
    }

    public void genPage()
    {
        if (page == null) {
            page = 1;
        }
        if (rows == null) {
            rows = 20;
        }
        if(start==null) {
            start = (page - 1) * rows;
        }
        if (start < 0) {
            start = 0;
        }
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
