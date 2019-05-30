package com.micro.user.model.pojo;

import com.micro.user.model.BasePojo;
import java.util.Date;

public class UserWallet extends BasePojo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_wallet.id
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_wallet.user_id
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_wallet.user_name
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    private String userName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_wallet.user_mobile
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    private String userMobile;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_wallet.amount
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    private Long amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_wallet.freeze_amount
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    private Long freezeAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_wallet.create_time
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_wallet.modified_time
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    private Date modifiedTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_wallet.id
     *
     * @return the value of user_wallet.id
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_wallet.id
     *
     * @param id the value for user_wallet.id
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_wallet.user_id
     *
     * @return the value of user_wallet.user_id
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_wallet.user_id
     *
     * @param userId the value for user_wallet.user_id
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_wallet.user_name
     *
     * @return the value of user_wallet.user_name
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_wallet.user_name
     *
     * @param userName the value for user_wallet.user_name
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_wallet.user_mobile
     *
     * @return the value of user_wallet.user_mobile
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public String getUserMobile() {
        return userMobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_wallet.user_mobile
     *
     * @param userMobile the value for user_wallet.user_mobile
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile == null ? null : userMobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_wallet.amount
     *
     * @return the value of user_wallet.amount
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_wallet.amount
     *
     * @param amount the value for user_wallet.amount
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_wallet.freeze_amount
     *
     * @return the value of user_wallet.freeze_amount
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public Long getFreezeAmount() {
        return freezeAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_wallet.freeze_amount
     *
     * @param freezeAmount the value for user_wallet.freeze_amount
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public void setFreezeAmount(Long freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_wallet.create_time
     *
     * @return the value of user_wallet.create_time
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_wallet.create_time
     *
     * @param createTime the value for user_wallet.create_time
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_wallet.modified_time
     *
     * @return the value of user_wallet.modified_time
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_wallet.modified_time
     *
     * @param modifiedTime the value for user_wallet.modified_time
     *
     * @mbggenerated Thu May 30 17:19:48 CST 2019
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}