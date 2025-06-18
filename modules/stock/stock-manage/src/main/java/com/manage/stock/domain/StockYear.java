package com.manage.stock.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manage.framework.aspectj.lang.annotation.Excel;
import com.manage.framework.web.domain.BaseEntity;

/**
 * 股票年数据对象 t_stock_year
 * 
 * @author gener
 * @date 2025-06-17
 */
public class StockYear extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 股票名称 */
    @Excel(name = "股票名称")
    private String stockName;

    /** 股票代码 */
    @Excel(name = "股票代码")
    private String stockCode;

    /** 年 */
    @Excel(name = "年")
    private Long statYear;

    /** 高光程度 */
    @Excel(name = "高光程度")
    private Long highlight;

    /** 最低股价 */
    @Excel(name = "最低股价")
    private BigDecimal minPrice;

    /** 最高股价 */
    @Excel(name = "最高股价")
    private BigDecimal maxPrice;

    /** 年初股价 */
    @Excel(name = "年初股价")
    private BigDecimal beginPrice;

    /** 年底股价 */
    @Excel(name = "年底股价")
    private BigDecimal endPrice;

    /** 市值(亿) */
    @Excel(name = "市值(亿)")
    private Long marketValue;

    /** 市盈率 */
    @Excel(name = "市盈率")
    private BigDecimal pe;

    /** 营收(亿) */
    @Excel(name = "营收(亿)")
    private BigDecimal gmv;

    /** 利润(亿) */
    @Excel(name = "利润(亿)")
    private BigDecimal profit;

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
    public void setStatYear(Long statYear) 
    {
        this.statYear = statYear;
    }

    public Long getStatYear() 
    {
        return statYear;
    }
    public void setHighlight(Long highlight) 
    {
        this.highlight = highlight;
    }

    public Long getHighlight() 
    {
        return highlight;
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
    public void setMarketValue(Long marketValue) 
    {
        this.marketValue = marketValue;
    }

    public Long getMarketValue() 
    {
        return marketValue;
    }
    public void setPe(BigDecimal pe) 
    {
        this.pe = pe;
    }

    public BigDecimal getPe() 
    {
        return pe;
    }
    public void setGmv(BigDecimal gmv) 
    {
        this.gmv = gmv;
    }

    public BigDecimal getGmv() 
    {
        return gmv;
    }
    public void setProfit(BigDecimal profit) 
    {
        this.profit = profit;
    }

    public BigDecimal getProfit() 
    {
        return profit;
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
            .append("stockName", getStockName())
            .append("stockCode", getStockCode())
            .append("statYear", getStatYear())
            .append("highlight", getHighlight())
            .append("minPrice", getMinPrice())
            .append("maxPrice", getMaxPrice())
            .append("beginPrice", getBeginPrice())
            .append("endPrice", getEndPrice())
            .append("marketValue", getMarketValue())
            .append("pe", getPe())
            .append("gmv", getGmv())
            .append("profit", getProfit())
            .append("note", getNote())
            .append("createTime", getCreateTime())
            .toString();
    }
}
