package com.manage.stock.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manage.framework.aspectj.lang.annotation.Excel;
import com.manage.framework.web.domain.BaseEntity;

/**
 * 股票天数据对象 t_stock_day
 * 
 * @author gener
 * @date 2025-06-18
 */
public class StockDay extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 关注标识 1-关注 */
    @Excel(name = "关注标识 1-关注")
    private Long careFlag;

    /** 股票名称 */
    @Excel(name = "股票名称")
    private String stockName;

    /** 股票代码 */
    @Excel(name = "股票代码")
    private String stockCode;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dayDate;

    /** 当天开始股价 */
    @Excel(name = "当天开始股价")
    private BigDecimal beginPrice;

    /** 当天结束股价 */
    @Excel(name = "当天结束股价")
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

    /** 委比,(委买手数-委卖手数)/(委买手数+委卖手数)×100% */
    @Excel(name = "委比,(委买手数-委卖手数)/(委买手数+委卖手数)×100%")
    private String orderRate;

    /** 主力流入资金(亿) */
    @Excel(name = "主力流入资金(亿)")
    private BigDecimal mainInAmount;

    /** 主力净流入(亿) */
    @Excel(name = "主力净流入(亿)")
    private BigDecimal mainTransAmount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCareFlag(Long careFlag) 
    {
        this.careFlag = careFlag;
    }

    public Long getCareFlag() 
    {
        return careFlag;
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
    public void setOrderRate(String orderRate) 
    {
        this.orderRate = orderRate;
    }

    public String getOrderRate() 
    {
        return orderRate;
    }
    public void setMainInAmount(BigDecimal mainInAmount) 
    {
        this.mainInAmount = mainInAmount;
    }

    public BigDecimal getMainInAmount() 
    {
        return mainInAmount;
    }
    public void setMainTransAmount(BigDecimal mainTransAmount) 
    {
        this.mainTransAmount = mainTransAmount;
    }

    public BigDecimal getMainTransAmount() 
    {
        return mainTransAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("careFlag", getCareFlag())
            .append("stockName", getStockName())
            .append("stockCode", getStockCode())
            .append("dayDate", getDayDate())
            .append("beginPrice", getBeginPrice())
            .append("endPrice", getEndPrice())
            .append("minPrice", getMinPrice())
            .append("maxPrice", getMaxPrice())
            .append("increaseRate", getIncreaseRate())
            .append("note", getNote())
            .append("turnoverRate", getTurnoverRate())
            .append("turnoverAmount", getTurnoverAmount())
            .append("orderRate", getOrderRate())
            .append("mainInAmount", getMainInAmount())
            .append("mainTransAmount", getMainTransAmount())
            .append("createTime", getCreateTime())
            .toString();
    }
}
