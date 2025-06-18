package com.manage.stock.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.manage.framework.aspectj.lang.annotation.Excel;
import com.manage.framework.web.domain.BaseEntity;

/**
 * 证券指数周数据对象 t_bourse_week
 * 
 * @author gener
 * @date 2025-06-18
 */
public class BourseWeek extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 变幅大标识 -1-大跌 1-大涨 */
    @Excel(name = "变幅大标识 -1-大跌 1-大涨")
    private Long hignFlag;

    /** 证券交易所 */
    @Excel(name = "证券交易所")
    private String bourseName;

    /** 证券交易所编码 */
    @Excel(name = "证券交易所编码")
    private String bourseCode;

    /** 周期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "周期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date weekDate;

    /** 当周开始指数 */
    @Excel(name = "当周开始指数")
    private BigDecimal beginPoint;

    /** 当周结束指数 */
    @Excel(name = "当周结束指数")
    private BigDecimal endPoint;

    /** 变幅百分比 */
    @Excel(name = "变幅百分比")
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

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setHignFlag(Long hignFlag) 
    {
        this.hignFlag = hignFlag;
    }

    public Long getHignFlag() 
    {
        return hignFlag;
    }
    public void setBourseName(String bourseName) 
    {
        this.bourseName = bourseName;
    }

    public String getBourseName() 
    {
        return bourseName;
    }
    public void setBourseCode(String bourseCode) 
    {
        this.bourseCode = bourseCode;
    }

    public String getBourseCode() 
    {
        return bourseCode;
    }
    public void setWeekDate(Date weekDate) 
    {
        this.weekDate = weekDate;
    }

    public Date getWeekDate() 
    {
        return weekDate;
    }
    public void setBeginPoint(BigDecimal beginPoint) 
    {
        this.beginPoint = beginPoint;
    }

    public BigDecimal getBeginPoint() 
    {
        return beginPoint;
    }
    public void setEndPoint(BigDecimal endPoint) 
    {
        this.endPoint = endPoint;
    }

    public BigDecimal getEndPoint() 
    {
        return endPoint;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("hignFlag", getHignFlag())
            .append("bourseName", getBourseName())
            .append("bourseCode", getBourseCode())
            .append("weekDate", getWeekDate())
            .append("beginPoint", getBeginPoint())
            .append("endPoint", getEndPoint())
            .append("increaseRate", getIncreaseRate())
            .append("note", getNote())
            .append("turnoverRate", getTurnoverRate())
            .append("turnoverAmount", getTurnoverAmount())
            .append("createTime", getCreateTime())
            .toString();
    }
}
