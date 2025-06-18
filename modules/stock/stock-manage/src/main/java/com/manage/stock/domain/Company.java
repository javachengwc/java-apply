package com.manage.stock.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manage.framework.aspectj.lang.annotation.Excel;
import com.manage.framework.web.domain.BaseEntity;

/**
 * 公司对象 t_company
 * 
 * @author gener
 * @date 2025-06-17
 */
public class Company extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String companyName;

    /** 省份名称 */
    @Excel(name = "省份名称")
    private String provinceName;

    /** 股票名字,多个以','号分隔 */
    @Excel(name = "股票名字,多个以','号分隔")
    private String stockNames;

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
    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }
    public void setProvinceName(String provinceName) 
    {
        this.provinceName = provinceName;
    }

    public String getProvinceName() 
    {
        return provinceName;
    }
    public void setStockNames(String stockNames) 
    {
        this.stockNames = stockNames;
    }

    public String getStockNames() 
    {
        return stockNames;
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
            .append("companyName", getCompanyName())
            .append("provinceName", getProvinceName())
            .append("stockNames", getStockNames())
            .append("note", getNote())
            .append("createTime", getCreateTime())
            .toString();
    }
}
