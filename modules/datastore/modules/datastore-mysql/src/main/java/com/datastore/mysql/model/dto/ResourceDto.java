package com.datastore.mysql.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResourceDto implements Serializable {

    private Integer id;

    private String name;

    private Integer parentId;

    private Integer isShow;

    private String tag;

    private String path;

    private String icon;

    private Integer type;

    private Integer sort;
}