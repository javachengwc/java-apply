package com.manage.stock.domain;

import java.math.BigDecimal;

import lombok.Data;
import lombok.ToString;
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
@Data
@ToString
public class StockYear extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
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

    /** 涨幅 */
    @Excel(name = "涨幅")
    private BigDecimal increaseRate;

    /** 市值(亿) */
    @Excel(name = "市值(亿)")
    private Long marketValue;

    /** 市盈率 */
    @Excel(name = "市盈率")
    private BigDecimal pe;

    /** 市净率 */
    @Excel(name = "市净率")
    private BigDecimal pb;

    /** 营收(亿) */
    @Excel(name = "营收(亿)")
    private BigDecimal gmv;

    /** 利润(亿) */
    @Excel(name = "利润(亿)")
    private BigDecimal profit;

	/** 分红(亿) */
    @Excel(name = "分红(亿)")
    private BigDecimal dividend;

    /** 备注 */
    @Excel(name = "备注")
    private String note;
}
