package com.manage.stock.domain;

import lombok.Data;
import lombok.ToString;
import com.manage.framework.aspectj.lang.annotation.Excel;
import com.manage.framework.web.domain.BaseEntity;

/**
 * 基金对象 t_fund
 * 
 * @author gener
 * @date 2025-06-18
 */
@Data
@ToString
public class Fund extends BaseEntity
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

    /** 交易方式 T+0,T+1 */
    @Excel(name = "交易方式 T+0,T+1")
    private String tradeMode;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

}
