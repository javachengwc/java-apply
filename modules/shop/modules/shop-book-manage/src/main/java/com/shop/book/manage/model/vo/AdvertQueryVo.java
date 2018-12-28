package com.shop.book.manage.model.vo;

import com.shop.base.model.PageParam;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AdvertQueryVo extends PageParam {

    @ApiModelProperty("开始时间 格式:yyyy-MM-dd")
    private String beginDate;

    @ApiModelProperty("结束时间 格式:yyyy-MM-dd")
    private String endDate;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}