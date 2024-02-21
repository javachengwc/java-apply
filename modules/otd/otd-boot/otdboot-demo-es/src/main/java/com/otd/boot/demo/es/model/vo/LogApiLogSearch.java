package com.otd.boot.demo.es.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LogApiLogSearch implements Serializable {

    private Integer page = 1;// -- 当前页

    private Integer rows = 10;// -- 每页记录数

    private Integer modelId;

    private String modelName;

    private String sysName;

    private Date startTime;

    private Date endTime;

    private String params;

    private Long vistorId;

    private String resultCode;

    private String exception;

}
