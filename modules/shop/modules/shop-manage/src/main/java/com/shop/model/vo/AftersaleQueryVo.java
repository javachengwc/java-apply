package com.shop.model.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 售后维权投诉查询类
 */
public class AftersaleQueryVo implements Serializable{

    //公共查询字段
    //售后维权投诉id
    private Long id;

    private String orderId;

    private Integer fromSource;

    private String productId;

    private String applyTimeBegin;

    private String applyTimeEnd;

    private Long userId;

    private String userName;

    private Long shopId;

    private String shopName;

    //状态
    private Integer state;

    //售后退款类型或投诉类型
    private Integer type;

    //维权查询字段
    //维权对应的售后id
    private Long aftersaleId;

    //维权发起方
    private Integer safeguardStarter;

    //投诉查询字段
    //投诉结果
    private Integer complainResult;

    private Integer page;

    private Integer start;

    private Integer rows;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getApplyTimeBegin() {
        return applyTimeBegin;
    }

    public void setApplyTimeBegin(String applyTimeBegin) {
        this.applyTimeBegin = applyTimeBegin;
    }

    public String getApplyTimeEnd() {
        return applyTimeEnd;
    }

    public void setApplyTimeEnd(String applyTimeEnd) {
        this.applyTimeEnd = applyTimeEnd;
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

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getAftersaleId() {
        return aftersaleId;
    }

    public void setAftersaleId(Long aftersaleId) {
        this.aftersaleId = aftersaleId;
    }

    public Integer getSafeguardStarter() {
        return safeguardStarter;
    }

    public void setSafeguardStarter(Integer safeguardStarter) {
        this.safeguardStarter = safeguardStarter;
    }

    public Integer getComplainResult() {
        return complainResult;
    }

    public void setComplainResult(Integer complainResult) {
        this.complainResult = complainResult;
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

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
