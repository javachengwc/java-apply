package com.sharding.bootdemo.model.vo;

import com.shop.base.model.PageParam;
import com.util.date.CalendarUtil;
import com.util.date.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

@ApiModel(description = "订单查询条件", value = "orderQueryVo")
@Data
public class OrderQueryVo extends PageParam {

    @ApiModelProperty(name = "orderId", value = "订单id")
    private Long orderId;

    @ApiModelProperty(name = "statu", value = "订单状态")
    private Integer statu;

    @ApiModelProperty(name = "userId", value = "用户id")
    private Long userId;

    @ApiModelProperty(name = "shopId", value = "店铺id")
    private Long shopId;

    @ApiModelProperty(name = "beginTime", value = "开始日期  yyyy-MM-dd")
    private String beginTime;

    @ApiModelProperty(name = "endTime", value = "结束日期  yyyy-MM-dd")
    private String endTime;

    public void ready() {
        this.genPage();
        if(!DateUtil.checkDate(beginTime,DateUtil.FMT_YMD)) {
            beginTime=null;
        }
        if(DateUtil.checkDate(endTime,DateUtil.FMT_YMD)) {
            Date endDate= DateUtil.getDate(endTime,DateUtil.FMT_YMD);
            Date cdnEndDate = CalendarUtil.addDates(endDate,1);
            String cdnEndStr =DateUtil.formatDate(cdnEndDate,DateUtil.FMT_YMD);
            this.endTime=cdnEndStr;
        }else {
            this.endTime=null;
        }
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
