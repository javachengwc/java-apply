package com.otd.boot.plat.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FactoryVo implements Serializable {

    private Long id;

    private String factoryNo;

    private String factoryName;

    private String factoryAddress;

    private String legalPerson;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;
}
