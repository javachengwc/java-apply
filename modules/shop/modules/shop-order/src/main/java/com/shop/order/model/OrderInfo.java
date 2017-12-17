package com.shop.order.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

@ApiModel(description = "订单信息", value = "orderInfo")
public class OrderInfo {

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "orderNo", value = "订单编号")
    private String orderNo;

    @ApiModelProperty(name = "fromSource", value = "订单来源: 1--PC,2--无线")
    private Integer fromSource;

    @ApiModelProperty(name = "statu", value = "订单状态: 0:初始 1:待支付 2:待发货 3:已发货 5:交易成功 7:交易关闭")
    private Integer statu;

    @ApiModelProperty(name = "userId", value = "买家Id")
    private Long userId;

    @ApiModelProperty(name = "userName", value = "买家姓名")
    private String userName;

    @ApiModelProperty(name = "userMobile", value = "买家手机号")
    private String userMobile;

    @ApiModelProperty(name = "shopId", value = "店铺Id")
    private Long shopId;

    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;

    @ApiModelProperty(name = "isOverbuy", value = "是否超卖: 0,正常 1,超卖")
    private Integer isOverbuy;

    @ApiModelProperty(name = "isCancel", value = "是否取消 0--否,1--是")
    private Integer isCancel;

    @ApiModelProperty(name = "cancelTime", value = "取消时间")
    private Date cancelTime;

    @ApiModelProperty(name = "cancelTimeStr", value = "取消时间 yyyy-MM-dd HH:mm:ss")
    private String cancelTimeStr;

    @ApiModelProperty(name = "priceAmount", value = "订单价格")
    private Long priceAmount;

    @ApiModelProperty(name = "postage", value = "邮费")
    private Long postage;

    @ApiModelProperty(name = "exchangeScore", value = "兑换的积分数量")
    private Integer exchangeScore;

    @ApiModelProperty(name = "exchangeCash", value = "兑换的金额")
    private Long exchangeCash;

    @ApiModelProperty(name = "couponNo", value = "优惠券编码")
    private String couponNo;

    @ApiModelProperty(name = "couponPrice", value = "优惠券金额")
    private Long couponPrice;

    @ApiModelProperty(name = "discountId", value = "活动id")
    private Integer discountId;

    @ApiModelProperty(name = "discountType", value = "活动类型")
    private Integer discountType;

    @ApiModelProperty(name = "discountPrice", value = "活动优惠的价格")
    private Long discountPrice;

    @ApiModelProperty(name = "payState", value = "支付状态: 0:未支付 1:支付成功 2:支付失败")
    private Integer payState;

    @ApiModelProperty(name = "payTime", value = "支付时间")
    private Date payTime;

    @ApiModelProperty(name = "payTimeStr", value = "支付时间 yyyy-MM-dd HH:mm:ss")
    private String payTimeStr;

    @ApiModelProperty(name = "payAccount", value = "支付账号")
    private String payAccount;

    @ApiModelProperty(name = "tradeNo", value = "第三方交易系统流水号")
    private String tradeNo;

    @ApiModelProperty(name = "payChannel", value = "支付渠道 0:无 1:支付宝 2:微信支付 3:网银")
    private Integer payChannel;

    @ApiModelProperty(name = "receiverProvinceId", value = "收货人所属省ID")
    private Integer receiverProvinceId;

    @ApiModelProperty(name = "receiverProvinceName", value = "收货人所属省名称")
    private String receiverProvinceName;

    @ApiModelProperty(name = "receiverCityId", value = "收货人所属城市ID")
    private Integer receiverCityId;

    @ApiModelProperty(name = "receiverCityName", value = "收货人所属城市名称")
    private String receiverCityName;

    @ApiModelProperty(name = "receiverAreaId", value = "收货人所属区县ID")
    private Integer receiverAreaId;

    @ApiModelProperty(name = "receiverAreaName", value = "收货人所属区县名称")
    private String receiverAreaName;

    @ApiModelProperty(name = "receiverAddress", value = "收货人地址")
    private String receiverAddress;

    @ApiModelProperty(name = "receiverName", value = "收货人姓名")
    private String receiverName;

    @ApiModelProperty(name = "receiverMobile", value = "收货人手机号")
    private String receiverMobile;

    @ApiModelProperty(name = "isReceive", value = "是否收货 0--未收货 1--已收货")
    private Integer isReceive;

    @ApiModelProperty(name = "receiveTime", value = "收货时间")
    private Date receiveTime;

    @ApiModelProperty(name = "receiveTimeStr", value = "收货时间 yyyy-MM-dd HH:mm:ss")
    private String receiveTimeStr;

    @ApiModelProperty(name = "deliverTime", value = "发货时间")
    private Date deliverTime;

    @ApiModelProperty(name = "deliverTimeStr", value = "发货时间 yyyy-MM-dd HH:mm:ss")
    private String deliverTimeStr;

    @ApiModelProperty(name = "deliverTimeLimit", value = "发货时限小时")
    private Integer deliverTimeLimit;

    @ApiModelProperty(name = "remindDeliverCount", value = "买家提醒卖家发货的次数")
    private Integer remindDeliverCount;

    @ApiModelProperty(name = "isDelive", value = "是否发货 0--未发货 1--已发货")
    private Integer isDelive;

    @ApiModelProperty(name = "isDeliveTimeout", value = "是否发货超时 0--否 1--是")
    private Integer isDeliveTimeout;

    @ApiModelProperty(name = "expressId", value = "快递公司Id")
    private Integer expressId;

    @ApiModelProperty(name = "expressName", value = "快递公司名称")
    private String expressName;

    @ApiModelProperty(name = "expressNo", value = "货运单号")
    private String expressNo;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "createTimeStr", value = "创建时间 yyyy-MM-dd HH:mm:ss")
    private String createTimeStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getFromSource() {
        return fromSource;
    }

    public void setFromSource(Integer fromSource) {
        this.fromSource = fromSource;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
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

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
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

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelTimeStr() {
        return cancelTimeStr;
    }

    public void setCancelTimeStr(String cancelTimeStr) {
        this.cancelTimeStr = cancelTimeStr;
    }

    public Long getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(Long priceAmount) {
        this.priceAmount = priceAmount;
    }

    public Long getPostage() {
        return postage;
    }

    public void setPostage(Long postage) {
        this.postage = postage;
    }

    public Integer getExchangeScore() {
        return exchangeScore;
    }

    public void setExchangeScore(Integer exchangeScore) {
        this.exchangeScore = exchangeScore;
    }

    public Long getExchangeCash() {
        return exchangeCash;
    }

    public void setExchangeCash(Long exchangeCash) {
        this.exchangeCash = exchangeCash;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public Long getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(Long couponPrice) {
        this.couponPrice = couponPrice;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayTimeStr() {
        return payTimeStr;
    }

    public void setPayTimeStr(String payTimeStr) {
        this.payTimeStr = payTimeStr;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    public Integer getReceiverProvinceId() {
        return receiverProvinceId;
    }

    public void setReceiverProvinceId(Integer receiverProvinceId) {
        this.receiverProvinceId = receiverProvinceId;
    }

    public String getReceiverProvinceName() {
        return receiverProvinceName;
    }

    public void setReceiverProvinceName(String receiverProvinceName) {
        this.receiverProvinceName = receiverProvinceName;
    }

    public Integer getReceiverCityId() {
        return receiverCityId;
    }

    public void setReceiverCityId(Integer receiverCityId) {
        this.receiverCityId = receiverCityId;
    }

    public String getReceiverCityName() {
        return receiverCityName;
    }

    public void setReceiverCityName(String receiverCityName) {
        this.receiverCityName = receiverCityName;
    }

    public Integer getReceiverAreaId() {
        return receiverAreaId;
    }

    public void setReceiverAreaId(Integer receiverAreaId) {
        this.receiverAreaId = receiverAreaId;
    }

    public String getReceiverAreaName() {
        return receiverAreaName;
    }

    public void setReceiverAreaName(String receiverAreaName) {
        this.receiverAreaName = receiverAreaName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public Integer getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(Integer isReceive) {
        this.isReceive = isReceive;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReceiveTimeStr() {
        return receiveTimeStr;
    }

    public void setReceiveTimeStr(String receiveTimeStr) {
        this.receiveTimeStr = receiveTimeStr;
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getDeliverTimeStr() {
        return deliverTimeStr;
    }

    public void setDeliverTimeStr(String deliverTimeStr) {
        this.deliverTimeStr = deliverTimeStr;
    }

    public Integer getDeliverTimeLimit() {
        return deliverTimeLimit;
    }

    public void setDeliverTimeLimit(Integer deliverTimeLimit) {
        this.deliverTimeLimit = deliverTimeLimit;
    }

    public Integer getRemindDeliverCount() {
        return remindDeliverCount;
    }

    public void setRemindDeliverCount(Integer remindDeliverCount) {
        this.remindDeliverCount = remindDeliverCount;
    }

    public Integer getIsDelive() {
        return isDelive;
    }

    public void setIsDelive(Integer isDelive) {
        this.isDelive = isDelive;
    }

    public Integer getIsDeliveTimeout() {
        return isDeliveTimeout;
    }

    public void setIsDeliveTimeout(Integer isDeliveTimeout) {
        this.isDeliveTimeout = isDeliveTimeout;
    }

    public Integer getExpressId() {
        return expressId;
    }

    public void setExpressId(Integer expressId) {
        this.expressId = expressId;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
