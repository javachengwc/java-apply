package com.manage.stock.domain.vo;

import com.manage.framework.aspectj.lang.annotation.Excel;
import com.manage.stock.domain.CompanyStock;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CompanyStockVo extends CompanyStock {

    private static final long serialVersionUID = 1L;

    /** 公司简介 */
    @Excel(name = "公司简介")
    private String companyIntroduce;

}
