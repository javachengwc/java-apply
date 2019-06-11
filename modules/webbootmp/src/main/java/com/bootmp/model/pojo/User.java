package com.bootmp.model.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Long id;

    private String name;

    private String mobile;

    private Integer status;

    private Date createTime;

    private Date modifiedTime;

}