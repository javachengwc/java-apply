package com.manage.stock.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
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
@Data
@ToString
public class StockDay extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
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
    private BigDecimal increaseRate;

    /** 振幅 */
    @Excel(name = "振幅")
    private BigDecimal changeRate;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** 换手率 */
    @Excel(name = "换手率")
    private BigDecimal turnoverRate;

    /** 成交额(亿) */
    @Excel(name = "成交额(亿)")
    private BigDecimal turnoverAmount;

    /** 委比,(委买手数-委卖手数)/(委买手数+委卖手数)×100% */
    @Excel(name = "委比,(委买手数-委卖手数)/(委买手数+委卖手数)×100%")
    private BigDecimal orderRate;

    /** 主力流入资金(亿) */
    @Excel(name = "主力流入资金(亿)")
    private BigDecimal mainInAmount;

    /** 主力净流入(亿) */
    @Excel(name = "主力净流入(亿)")
    private BigDecimal mainTransAmount;

}
