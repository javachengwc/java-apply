package com.micro.order.model.pojo;

import com.micro.order.model.BasePojo;
import java.util.Date;

public class ShopOrder extends BasePojo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.id
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.order_sn
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private String orderSn;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.buyer_id
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private Long buyerId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.buyer_name
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private String buyerName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.buyer_mobile
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private String buyerMobile;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.statu
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private Integer statu;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.goods_id
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private Long goodsId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.goods_name
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private String goodsName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.goods_count
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private Integer goodsCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.total_amount
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private Long totalAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.create_time
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column shop_order.modified_time
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    private Date modifiedTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.id
     *
     * @return the value of shop_order.id
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.id
     *
     * @param id the value for shop_order.id
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.order_sn
     *
     * @return the value of shop_order.order_sn
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public String getOrderSn() {
        return orderSn;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.order_sn
     *
     * @param orderSn the value for shop_order.order_sn
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn == null ? null : orderSn.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.buyer_id
     *
     * @return the value of shop_order.buyer_id
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public Long getBuyerId() {
        return buyerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.buyer_id
     *
     * @param buyerId the value for shop_order.buyer_id
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.buyer_name
     *
     * @return the value of shop_order.buyer_name
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.buyer_name
     *
     * @param buyerName the value for shop_order.buyer_name
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName == null ? null : buyerName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.buyer_mobile
     *
     * @return the value of shop_order.buyer_mobile
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public String getBuyerMobile() {
        return buyerMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.buyer_mobile
     *
     * @param buyerMobile the value for shop_order.buyer_mobile
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setBuyerMobile(String buyerMobile) {
        this.buyerMobile = buyerMobile == null ? null : buyerMobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.statu
     *
     * @return the value of shop_order.statu
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public Integer getStatu() {
        return statu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.statu
     *
     * @param statu the value for shop_order.statu
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.goods_id
     *
     * @return the value of shop_order.goods_id
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.goods_id
     *
     * @param goodsId the value for shop_order.goods_id
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.goods_name
     *
     * @return the value of shop_order.goods_name
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.goods_name
     *
     * @param goodsName the value for shop_order.goods_name
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.goods_count
     *
     * @return the value of shop_order.goods_count
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public Integer getGoodsCount() {
        return goodsCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.goods_count
     *
     * @param goodsCount the value for shop_order.goods_count
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.total_amount
     *
     * @return the value of shop_order.total_amount
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public Long getTotalAmount() {
        return totalAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.total_amount
     *
     * @param totalAmount the value for shop_order.total_amount
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.create_time
     *
     * @return the value of shop_order.create_time
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.create_time
     *
     * @param createTime the value for shop_order.create_time
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column shop_order.modified_time
     *
     * @return the value of shop_order.modified_time
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column shop_order.modified_time
     *
     * @param modifiedTime the value for shop_order.modified_time
     *
     * @mbggenerated Thu May 30 15:45:36 CST 2019
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}