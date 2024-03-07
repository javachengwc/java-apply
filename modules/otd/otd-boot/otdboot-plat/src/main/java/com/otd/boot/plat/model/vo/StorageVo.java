package com.otd.boot.plat.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StorageVo implements Serializable {

    private Long id;

    private String storageCode;

    private String storageName;

    private Integer storageType;

    private String storageManager;

    private String warehouseCode;

    private String contactTel;

    private String provinceCode;

    private String provinceName;

    private String cityCode;

    private String cityName;

    private String countyCode;

    private String countyName;

    private String address;

    private String remark;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;
}
