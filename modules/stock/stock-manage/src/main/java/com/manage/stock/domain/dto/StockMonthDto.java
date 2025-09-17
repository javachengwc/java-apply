package com.manage.stock.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class StockMonthDto {

    private String stockName;

    private String stockCode;

    /** 月份从 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date monthDateStart;

    /** 月份到 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date monthDateEnd;
}
