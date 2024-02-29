package com.otd.boot.plat.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WarehouseVo implements Serializable {

    private Long id;

    private String warehouseCode;

    private String warehouseName;

    private Integer isEnable;

    private String address;

    private String contactPerson;

    private String contactTel;

    private String contactEmail;

    private String logisticsProviderNo;

    private Integer diyCapacity;

    private String vkorgs;

    private Integer lineBodyQps;

    private String workStartTime;

    private String workEndTime;

    private BigDecimal inWarehouseTime;

    private String legalPerson;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;
}
