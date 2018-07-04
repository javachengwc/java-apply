package com.shop.user.model.pojo;

import java.util.Date;

public class UserLogin {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.id
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.user_id
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.login_from
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private Integer loginFrom;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.login_tag
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private Integer loginTag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.ip
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private String ip;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.login_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private Date loginTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.logout_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private Date logoutTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.app
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private String app;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.app_version
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private String appVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.device_os
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private String deviceOs;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.device_os_version
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private String deviceOsVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.device_token
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private String deviceToken;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.create_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_login.modified_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    private Date modifiedTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.id
     *
     * @return the value of user_login.id
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.id
     *
     * @param id the value for user_login.id
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.user_id
     *
     * @return the value of user_login.user_id
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.user_id
     *
     * @param userId the value for user_login.user_id
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.login_from
     *
     * @return the value of user_login.login_from
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public Integer getLoginFrom() {
        return loginFrom;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.login_from
     *
     * @param loginFrom the value for user_login.login_from
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setLoginFrom(Integer loginFrom) {
        this.loginFrom = loginFrom;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.login_tag
     *
     * @return the value of user_login.login_tag
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public Integer getLoginTag() {
        return loginTag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.login_tag
     *
     * @param loginTag the value for user_login.login_tag
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setLoginTag(Integer loginTag) {
        this.loginTag = loginTag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.ip
     *
     * @return the value of user_login.ip
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public String getIp() {
        return ip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.ip
     *
     * @param ip the value for user_login.ip
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.login_time
     *
     * @return the value of user_login.login_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.login_time
     *
     * @param loginTime the value for user_login.login_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.logout_time
     *
     * @return the value of user_login.logout_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public Date getLogoutTime() {
        return logoutTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.logout_time
     *
     * @param logoutTime the value for user_login.logout_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.app
     *
     * @return the value of user_login.app
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public String getApp() {
        return app;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.app
     *
     * @param app the value for user_login.app
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setApp(String app) {
        this.app = app == null ? null : app.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.app_version
     *
     * @return the value of user_login.app_version
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.app_version
     *
     * @param appVersion the value for user_login.app_version
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion == null ? null : appVersion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.device_os
     *
     * @return the value of user_login.device_os
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public String getDeviceOs() {
        return deviceOs;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.device_os
     *
     * @param deviceOs the value for user_login.device_os
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs == null ? null : deviceOs.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.device_os_version
     *
     * @return the value of user_login.device_os_version
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public String getDeviceOsVersion() {
        return deviceOsVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.device_os_version
     *
     * @param deviceOsVersion the value for user_login.device_os_version
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setDeviceOsVersion(String deviceOsVersion) {
        this.deviceOsVersion = deviceOsVersion == null ? null : deviceOsVersion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.device_token
     *
     * @return the value of user_login.device_token
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public String getDeviceToken() {
        return deviceToken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.device_token
     *
     * @param deviceToken the value for user_login.device_token
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken == null ? null : deviceToken.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.create_time
     *
     * @return the value of user_login.create_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.create_time
     *
     * @param createTime the value for user_login.create_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_login.modified_time
     *
     * @return the value of user_login.modified_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public Date getModifiedTime() {
        return modifiedTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_login.modified_time
     *
     * @param modifiedTime the value for user_login.modified_time
     *
     * @mbggenerated Wed Jul 04 09:43:43 CST 2018
     */
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}