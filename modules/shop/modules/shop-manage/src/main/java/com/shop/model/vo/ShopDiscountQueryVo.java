package com.shop.model.vo;

import com.shop.util.DateTimeUtil;
import com.util.date.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * 商品折扣查询类
 */
public class ShopDiscountQueryVo  implements Serializable {

    private Integer shopId;

    private String discountName;

    private Integer discountState;

    private Integer discountType;

    private Integer discountRange;

    //创建开始时间
    private String createTimeBegin;

    //创建结束时间
    private String createTimeEnd;

    //折扣开始时间从
    private String beginTimeBegin;

    //折扣开始时间到
    private String beginTimeEnd;

    //折扣结束时间从
    private String endTimeBegin;

    //折扣结束时间到
    private String endTimeEnd;

    private Integer page;

    private Integer start;

    private Integer rows;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public Integer getDiscountState() {
        return discountState;
    }

    public void setDiscountState(Integer discountState) {
        this.discountState = discountState;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public String getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(String createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getBeginTimeBegin() {
        return beginTimeBegin;
    }

    public void setBeginTimeBegin(String beginTimeBegin) {
        this.beginTimeBegin = beginTimeBegin;
    }

    public String getBeginTimeEnd() {
        return beginTimeEnd;
    }

    public void setBeginTimeEnd(String beginTimeEnd) {
        this.beginTimeEnd = beginTimeEnd;
    }

    public String getEndTimeBegin() {
        return endTimeBegin;
    }

    public void setEndTimeBegin(String endTimeBegin) {
        this.endTimeBegin = endTimeBegin;
    }

    public String getEndTimeEnd() {
        return endTimeEnd;
    }

    public void setEndTimeEnd(String endTimeEnd) {
        this.endTimeEnd = endTimeEnd;
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

    public Integer getDiscountRange() {
        return discountRange;
    }

    public void setDiscountRange(Integer discountRange) {
        this.discountRange = discountRange;
    }

    public void genDateParam()
    {
        try{
            if(!StringUtils.isBlank(beginTimeBegin))
            {
                beginTimeBegin = DateTimeUtil.getDateTime(beginTimeBegin, DateUtil.FMT_YMD).toString(DateUtil.FMT_YMD_HMS);
            }
            if(!StringUtils.isBlank(beginTimeEnd))
            {
                DateTime tmpDate = DateTimeUtil.getDateTime(beginTimeEnd,DateUtil.FMT_YMD);
                tmpDate= tmpDate.withTime(23,59,59,999);
                beginTimeEnd = tmpDate.toString(DateUtil.FMT_YMD_HMS);
            }
            if(!StringUtils.isBlank(createTimeBegin))
            {
                createTimeBegin = DateTimeUtil.getDateTime(createTimeBegin,DateUtil.FMT_YMD).toString(DateUtil.FMT_YMD_HMS);
            }
            if(!StringUtils.isBlank(createTimeEnd))
            {
                DateTime tmpDate = DateTimeUtil.getDateTime(createTimeEnd,DateUtil.FMT_YMD);
                tmpDate= tmpDate.withTime(23,59,59,999);
                createTimeEnd = tmpDate.toString(DateUtil.FMT_YMD_HMS);
            }
            if(!StringUtils.isBlank(endTimeBegin))
            {
                endTimeBegin = DateTimeUtil.getDateTime(endTimeBegin,DateUtil.FMT_YMD).toString(DateUtil.FMT_YMD_HMS);
            }
            if(!StringUtils.isBlank(endTimeEnd))
            {
                DateTime tmpDate = DateTimeUtil.getDateTime(endTimeEnd,DateUtil.FMT_YMD);
                tmpDate= tmpDate.withTime(23,59,59,999);
                endTimeEnd = tmpDate.toString(DateUtil.FMT_YMD_HMS);
            }
        }catch(Exception e)
        {

        }
    }

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

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
