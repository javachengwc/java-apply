package com.otd.boot.plat.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class LogisticsProviderVo implements Serializable {

    private Long id;

    private String logisticsProviderNo;

    private String logisticsProviderName;

    private String expressType;

    private BigDecimal firstWeight;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;
}
