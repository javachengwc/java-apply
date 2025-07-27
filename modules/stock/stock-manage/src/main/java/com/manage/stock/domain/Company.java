package com.manage.stock.domain;

import lombok.Data;
import lombok.ToString;
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
@Data
@ToString
public class Company extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 公司名称 */
    @Excel(name = "公司")
    private String companyName;

    /** 省份名称 */
    @Excel(name = "省份")
    private String provinceName;

    /** 股票名字,多个以','号分隔 */
    @Excel(name = "股票名字")
    private String stockNames;

    /** 简介 */
    @Excel(name = "简介")
    private String introduce;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

}
