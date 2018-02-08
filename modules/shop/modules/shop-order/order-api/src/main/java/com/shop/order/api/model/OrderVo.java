package com.shop.order.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(description = "订单创建信息", value = "orderVo")
public class OrderVo {

    @ApiModelProperty(name = "orderNo", value = "订单编号")
    private String orderNo;

    @ApiModelProperty(name = "fromSource", value = "订单来源: 1--PC,2--无线")
    private Integer fromSource;

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

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
