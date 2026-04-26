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
 * 基金月数据对象 t_fund_month
 * 
 * @author gener
 * @date 2025-06-19
 */
@Data
@ToString
public class FundMonth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 基金名称 */
    @Excel(name = "基金名称")
    private String fundName;

    /** 基金编码 */
    @Excel(name = "基金编码")
    private String fundCode;

    /** 月份 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "月份", width = 30, dateFormat = "yyyy-MM-dd")
    private Date monthDate;

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

}
