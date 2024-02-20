package com.otd.boot.finance.model.vo;

import com.model.base.PageParam;
import lombok.Data;

@Data
public class FinanceAccountSearch extends PageParam {

    private String custNo;

    private Integer paymentType;

    private String docType;

    private Integer comefrom;

    private Integer state;

    private String batchId;

    private String documentNumber;

    private String createTimeBegin;

    private String createTimeEnd;

}
