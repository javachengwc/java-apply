package com.otd.boot.finance.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class FinanceAccountVo implements Serializable {

    private String id;

    private Integer type;

    private Integer comefrom;

    private String custNo;

    private Integer paymentType;

    private String currency;

    private BigDecimal amount;

    private BigDecimal preTaxAmount;

    private BigDecimal taxAmount;

    private String itemText;

    private String batchId;

    private String fiscalYear;

    private String companyCode;

    private String documentNumber;

    private String docType;

    private Date documentDate;

    private Date postingDate;

    private Integer needCertify;

    private Integer state;

    private Integer clearFlag;

    private String failInfo;

    private Date invokeTime;

    private String restFrom;

    private String remark;

    private Integer retryCount;

    private Integer deleteFlag;

    private Date deleteTime;

    private String deleteBy;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;
}
