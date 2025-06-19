package com.manage.stock.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manage.framework.aspectj.lang.annotation.Excel;
import com.manage.framework.web.domain.BaseEntity;

/**
 * 基金天数据对象 t_fund_day
 * 
 * @author gener
 * @date 2025-06-19
 */
public class FundDay extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 基金名称 */
    @Excel(name = "基金名称")
    private String fundName;

    /** 基金编码 */
    @Excel(name = "基金编码")
    private String fundCode;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dayDate;

    /** 开盘价 */
    @Excel(name = "开盘价")
    private BigDecimal beginPrice;

    /** 收盘价 */
    @Excel(name = "收盘价")
    private BigDecimal endPrice;

    /** 涨幅 */
    @Excel(name = "涨幅")
    private String increaseRate;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFundName(String fundName) 
    {
        this.fundName = fundName;
    }

    public String getFundName() 
    {
        return fundName;
    }
    public void setFundCode(String fundCode) 
    {
        this.fundCode = fundCode;
    }

    public String getFundCode() 
    {
        return fundCode;
    }
    public void setDayDate(Date dayDate) 
    {
        this.dayDate = dayDate;
    }

    public Date getDayDate() 
    {
        return dayDate;
    }
    public void setBeginPrice(BigDecimal beginPrice) 
    {
        this.beginPrice = beginPrice;
    }

    public BigDecimal getBeginPrice() 
    {
        return beginPrice;
    }
    public void setEndPrice(BigDecimal endPrice) 
    {
        this.endPrice = endPrice;
    }

    public BigDecimal getEndPrice() 
    {
        return endPrice;
    }
    public void setIncreaseRate(String increaseRate) 
    {
        this.increaseRate = increaseRate;
    }

    public String getIncreaseRate() 
    {
        return increaseRate;
    }
    public void setNote(String note) 
    {
        this.note = note;
    }

    public String getNote() 
    {
        return note;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fundName", getFundName())
            .append("fundCode", getFundCode())
            .append("dayDate", getDayDate())
            .append("beginPrice", getBeginPrice())
            .append("endPrice", getEndPrice())
            .append("increaseRate", getIncreaseRate())
            .append("note", getNote())
            .append("createTime", getCreateTime())
            .toString();
    }
}
