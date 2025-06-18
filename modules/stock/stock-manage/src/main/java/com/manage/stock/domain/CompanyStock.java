package com.manage.stock.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class CompanyStock extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
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

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date modifyTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCompanyId(Long companyId) 
    {
        this.companyId = companyId;
    }

    public Long getCompanyId() 
    {
        return companyId;
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
    public void setStockMarketCode(String stockMarketCode) 
    {
        this.stockMarketCode = stockMarketCode;
    }

    public String getStockMarketCode() 
    {
        return stockMarketCode;
    }
    public void setCareValue(Long careValue) 
    {
        this.careValue = careValue;
    }

    public Long getCareValue() 
    {
        return careValue;
    }
    public void setIndustry(String industry) 
    {
        this.industry = industry;
    }

    public String getIndustry() 
    {
        return industry;
    }
    public void setHaveData(Long haveData) 
    {
        this.haveData = haveData;
    }

    public Long getHaveData() 
    {
        return haveData;
    }
    public void setPublicTime(Date publicTime) 
    {
        this.publicTime = publicTime;
    }

    public Date getPublicTime() 
    {
        return publicTime;
    }
    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }
    public void setRecordDay(String recordDay) 
    {
        this.recordDay = recordDay;
    }

    public String getRecordDay() 
    {
        return recordDay;
    }
    public void setDivvyDay(String divvyDay) 
    {
        this.divvyDay = divvyDay;
    }

    public String getDivvyDay() 
    {
        return divvyDay;
    }
    public void setModifyTime(Date modifyTime) 
    {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTime() 
    {
        return modifyTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("stockName", getStockName())
            .append("stockCode", getStockCode())
            .append("stockMarketCode", getStockMarketCode())
            .append("careValue", getCareValue())
            .append("industry", getIndustry())
            .append("haveData", getHaveData())
            .append("publicTime", getPublicTime())
            .append("tags", getTags())
            .append("recordDay", getRecordDay())
            .append("divvyDay", getDivvyDay())
            .append("createTime", getCreateTime())
            .append("modifyTime", getModifyTime())
            .toString();
    }
}
