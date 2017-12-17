package com.shop.activity.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

@ApiModel(description = "用户每天数据", value = "userDailyInfo")
public class UserDailyInfo {

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    @ApiModelProperty(name = "userId", value = "用户ID")
    private Long userId;

    @ApiModelProperty(name = "mobile", value = "手机号")
    private String mobile;

    @ApiModelProperty(name = "dayDate", value = "日期")
    private Date dayDate;

    @ApiModelProperty(name = "dayDateStr", value = "日期 yyyy-MM-dd")
    private String dayDateStr;

    @ApiModelProperty(name = "score", value = "积分")
    private Integer score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getDayDate() {
        return dayDate;
    }

    public void setDayDate(Date dayDate) {
        this.dayDate = dayDate;
    }

    public String getDayDateStr() {
        return dayDateStr;
    }

    public void setDayDateStr(String dayDateStr) {
        this.dayDateStr = dayDateStr;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
