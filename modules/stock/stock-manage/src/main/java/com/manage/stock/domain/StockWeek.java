package com.manage.stock.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manage.framework.aspectj.lang.annotation.Excel;
import com.manage.framework.web.domain.BaseEntity;

/**
 * 股票周数据对象 t_stock_week
 * 
 * @author gener
 * @date 2025-06-18
 */
public class StockWeek extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 大涨大跌标识 1-大涨 -1-大跌 */
    @Excel(name = "大涨大跌标识 1-大涨 -1-大跌")
    private Long hignFlag;

    /** 股票名称 */
    @Excel(name = "股票名称")
    private String stockName;

    /** 股票代码 */
    @Excel(name = "股票代码")
    private String stockCode;

    /** 周日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "周日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date weekDate;

    /** 当周开始股价 */
    @Excel(name = "当周开始股价")
    private BigDecimal beginPrice;

    /** 当周结束股价 */
    @Excel(name = "当周结束股价")
    private BigDecimal endPrice;

    /** 最低价 */
    @Excel(name = "最低价")
    private BigDecimal minPrice;

    /** 最高价 */
    @Excel(name = "最高价")
    private BigDecimal maxPrice;

    /** 涨幅 */
    @Excel(name = "涨幅")
    private String increaseRate;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** 换手率 */
    @Excel(name = "换手率")
    private String turnoverRate;

    /** 成交额(亿) */
    @Excel(name = "成交额(亿)")
    private BigDecimal turnoverAmount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setHignFlag(Long hignFlag) 
    {
        this.hignFlag = hignFlag;
    }

    public Long getHignFlag() 
    {
        return hignFlag;
    }
    public void setStockName(String stockName) 
    {
        this.stockName = stockName;
    }

    public String getStockName() 
    {
        return stockName;
    }
    public void setStockCode(String stockCode) 
    {
        this.stockCode = stockCode;
    }

    public String getStockCode() 
    {
        return stockCode;
    }
    public void setWeekDate(Date weekDate) 
    {
        this.weekDate = weekDate;
    }

    public Date getWeekDate() 
    {
        return weekDate;
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
    public void setMinPrice(BigDecimal minPrice) 
    {
        this.minPrice = minPrice;
    }

    public BigDecimal getMinPrice() 
    {
        return minPrice;
    }
    public void setMaxPrice(BigDecimal maxPrice) 
    {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMaxPrice() 
    {
        return maxPrice;
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
    public void setTurnoverRate(String turnoverRate) 
    {
        this.turnoverRate = turnoverRate;
    }

    public String getTurnoverRate() 
    {
        return turnoverRate;
    }
    public void setTurnoverAmount(BigDecimal turnoverAmount) 
    {
        this.turnoverAmount = turnoverAmount;
    }

    public BigDecimal getTurnoverAmount() 
    {
        return turnoverAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("hignFlag", getHignFlag())
            .append("stockName", getStockName())
            .append("stockCode", getStockCode())
            .append("weekDate", getWeekDate())
            .append("beginPrice", getBeginPrice())
            .append("endPrice", getEndPrice())
            .append("minPrice", getMinPrice())
            .append("maxPrice", getMaxPrice())
            .append("increaseRate", getIncreaseRate())
            .append("note", getNote())
            .append("turnoverRate", getTurnoverRate())
            .append("turnoverAmount", getTurnoverAmount())
            .append("createTime", getCreateTime())
            .toString();
    }
}
