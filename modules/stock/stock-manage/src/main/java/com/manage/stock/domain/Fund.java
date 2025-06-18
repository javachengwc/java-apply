package com.manage.stock.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manage.framework.aspectj.lang.annotation.Excel;
import com.manage.framework.web.domain.BaseEntity;

/**
 * 基金对象 t_fund
 * 
 * @author gener
 * @date 2025-06-18
 */
public class Fund extends BaseEntity
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

    /** 交易方式 T+0,T+1 */
    @Excel(name = "交易方式 T+0,T+1")
    private String tradeMode;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

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
    public void setTradeMode(String tradeMode) 
    {
        this.tradeMode = tradeMode;
    }

    public String getTradeMode() 
    {
        return tradeMode;
    }
    public void setNote(String note) 
    {
        this.note = note;
    }

    public String getNote() 
    {
        return note;
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
            .append("fundName", getFundName())
            .append("fundCode", getFundCode())
            .append("tradeMode", getTradeMode())
            .append("note", getNote())
            .append("createTime", getCreateTime())
            .append("modifyTime", getModifyTime())
            .toString();
    }
}
