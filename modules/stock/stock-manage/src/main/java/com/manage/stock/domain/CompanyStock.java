package com.manage.stock.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manage.framework.aspectj.lang.annotation.Excel;
import com.manage.framework.web.domain.BaseEntity;

/**
 * 公司股票对象 t_company_stock
 * 
 * @author gener
 * @date 2025-06-17
 */
@Data
@ToString
public class CompanyStock extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 公司ID */
    @Excel(name = "${companyId}")
    private Long companyId;

    /** 股票名称 */
    @Excel(name = "股票名称")
    private String stockName;

    /** 股票代码 */
    @Excel(name = "股票代码")
    private String stockCode;

    /** 股票交易所code */
    @Excel(name = "股票交易所code")
    private String stockMarketCode;

    /** 关注值,0-10 */
    @Excel(name = "关注值,0-10")
    private Long careValue;

    /** 行业 */
    @Excel(name = "行业")
    private String industry;

    /** 是否有数据 0-否,1-是 */
    @Excel(name = "是否有数据 0-否,1-是")
    private Long haveData;

    /** 上市时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上市时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publicTime;

    /** 标签，逗号分隔 */
    @Excel(name = "标签，逗号分隔")
    private String tags;

    /** 股权登记日 */
    @Excel(name = "股权登记日")
    private String recordDay;

    /** 分红日 */
    @Excel(name = "分红日")
    private String divvyDay;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

}
