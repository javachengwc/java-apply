package com.shop.order.model.pojo;

import java.util.Date;

public class OrderOperateRecord {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.id
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.order_no
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private String orderNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.pre_statu
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private Integer preStatu;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.cur_statu
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private Integer curStatu;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.operate_action
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private Integer operateAction;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.operate_desc
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private String operateDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.operator_id
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private Long operatorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.operator_name
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private String operatorName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.note
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private String note;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.create_time
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column order_operate_record.modified_time
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    private Date modifiedTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.id
     *
     * @return the value of order_operate_record.id
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.id
     *
     * @param id the value for order_operate_record.id
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.order_no
     *
     * @return the value of order_operate_record.order_no
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.order_no
     *
     * @param orderNo the value for order_operate_record.order_no
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.pre_statu
     *
     * @return the value of order_operate_record.pre_statu
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public Integer getPreStatu() {
        return preStatu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.pre_statu
     *
     * @param preStatu the value for order_operate_record.pre_statu
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setPreStatu(Integer preStatu) {
        this.preStatu = preStatu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.cur_statu
     *
     * @return the value of order_operate_record.cur_statu
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public Integer getCurStatu() {
        return curStatu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.cur_statu
     *
     * @param curStatu the value for order_operate_record.cur_statu
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setCurStatu(Integer curStatu) {
        this.curStatu = curStatu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.operate_action
     *
     * @return the value of order_operate_record.operate_action
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public Integer getOperateAction() {
        return operateAction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.operate_action
     *
     * @param operateAction the value for order_operate_record.operate_action
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setOperateAction(Integer operateAction) {
        this.operateAction = operateAction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.operate_desc
     *
     * @return the value of order_operate_record.operate_desc
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public String getOperateDesc() {
        return operateDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.operate_desc
     *
     * @param operateDesc the value for order_operate_record.operate_desc
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setOperateDesc(String operateDesc) {
        this.operateDesc = operateDesc == null ? null : operateDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.operator_id
     *
     * @return the value of order_operate_record.operator_id
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.operator_id
     *
     * @param operatorId the value for order_operate_record.operator_id
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.operator_name
     *
     * @return the value of order_operate_record.operator_name
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.operator_name
     *
     * @param operatorName the value for order_operate_record.operator_name
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.note
     *
     * @return the value of order_operate_record.note
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public String getNote() {
        return note;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.note
     *
     * @param note the value for order_operate_record.note
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.create_time
     *
     * @return the value of order_operate_record.create_time
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.create_time
     *
     * @param createTime the value for order_operate_record.create_time
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column order_operate_record.modified_time
     *
     * @return the value of order_operate_record.modified_time
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column order_operate_record.modified_time
     *
     * @param modifiedTime the value for order_operate_record.modified_time
     *
     * @mbggenerated Thu Feb 08 17:16:06 CST 2018
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}