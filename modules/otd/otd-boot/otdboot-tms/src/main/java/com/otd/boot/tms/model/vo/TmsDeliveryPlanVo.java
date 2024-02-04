package com.otd.boot.tms.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TmsDeliveryPlanVo implements Serializable {

    //serialVersionUID用于标识类的版本
    //显示指定serialVersionUID为固定值能确保即使在类定义发生变化的情况下，之前序列化的对象仍然可以被正确地反序列化，从而保持了数据的完整性和程序的稳定性。
    //如果不指定其值，Java虚拟机会根据类的详细信息自动生成一个,如果一个类的结构（如成员变量、方法等）发生了变化，那么它的serialVersionUID也会相应变化。
    //在反序列化时，JVM会检查序列化对象的serialVersionUID与当前类的serialVersionUID是否匹配，如果不匹配，就会抛出InvalidClassException异常。
    //为了避免因类的变化导致的不兼容性问题，最好显式定义serialVersionUID。
    private static final long serialVersionUID = -6121220395754911101L;

    private String id;

    private String deliveryNo;

    private String deliveryBatchNumber;

    private String warehouseCode;

    private String destProvince;

    private String destCity;

    private String destCounty;

    private String routeId;

    private String logisticsProviderCode;

    private Integer logisticsType;

    private Integer pickMode;

    private Integer tranType;

    private Integer printFlag;

    private Date printTime;

    private Integer turnWmsFlag;

    private Date turnWmsTime;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;
}
