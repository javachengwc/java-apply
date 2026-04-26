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
 * 证券指数天数据对象 t_bourse_day
 * 
 * @author gener
 * @date 2025-06-18
 */
@Data
@ToString
public class BourseDay extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
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

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dayDate;

    /** 当天开始指数 */
    @Excel(name = "当天开始指数")
    private BigDecimal beginPoint;

    /** 当天结束指数 */
    @Excel(name = "当天结束指数")
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

}
