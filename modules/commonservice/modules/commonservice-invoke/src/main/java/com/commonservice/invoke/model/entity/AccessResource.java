package com.commonservice.invoke.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@TableName("t_access_resource")
@Data
public class AccessResource implements Serializable {

    private Long id;

    private String name;

    private String note;

    private Integer sysId;

    private Integer cateId;

    private String httpMethod;

    private String contentType;

    private String resourceLink;

    private String header;

    private String reqDemo;

    private String respDemo;

    private Date createTime;

    private Date modifyTime;

}
