package com.bootmp.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {

    private Long id;

    private String name;

    private String mobile;

    private Integer status;

    private Date createTime;

    private Date modifiedTime;
}
