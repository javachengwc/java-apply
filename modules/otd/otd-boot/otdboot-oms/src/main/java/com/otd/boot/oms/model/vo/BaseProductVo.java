package com.otd.boot.oms.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BaseProductVo implements Serializable {

    private Long id;

    private String productCode;

    private String productName;

    private Integer physicalFlag;

    private Integer giftFlag;

    private Integer selfProductFlag;

    private BigDecimal volume;

    private BigDecimal weight;

    private String productType;

    private Integer needScan;

    private Integer onSale;

    private String taxCode;

    private BigDecimal taxRate;

    private String specifications;

    private String barcodeRemark;

    private BigDecimal benchmarkPrice;

    private String code69;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;
}
