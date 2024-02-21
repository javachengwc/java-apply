package com.otd.boot.demo.es.model.doc;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "log_api_log",type = "common_type")
@Data
public class LogApiLog {

    @Id
    private Long id;

    private Long vistorId;

    private String method;

    private String createBy;

    private String resultCode;

    private String uri;

    private String clientIP;

    private String sysName;

    private String serverIP;

    private String userAgent;

    private String params;

    private String exception;

    private Integer executionTime;

    private String  modelName;

    private String busiKey;

    private String className;

    private Integer modelId;

    private Date createTime;
}
