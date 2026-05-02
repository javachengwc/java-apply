package com.manage.stock.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class StockQueryParam {

    /** 股票名称 */
    private String stockName;

    /** 股票代码 */
    private String stockCode;

    /** 行业 */
    private String industry;

    /**
     * 查询方式，year-年数据查询 month-月数据查询
     * week-- 周数据查询 day-天数据查询
     */
    private String queryType;

    /** 年 */
    private Long year;

    /** 月份从 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /** 月份到 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
