package com.commonservice.invoke.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@TableName("t_access_resource")
@Data
public class AccessResource implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("path")
    private String path;

    @TableField("type")
    private Integer type;

    @TableField("note")
    private String note;

    @TableField("sys_id")
    private Integer sysId;

    @TableField("cate_id")
    private Integer cateId;

    @TableField("http_method")
    private String httpMethod;

    @TableField("content_type")
    private String contentType;

    @TableField("resource_link")
    private String resourceLink;

    @TableField("req_demo")
    private String reqDemo;

    @TableField("resp_demo")
    private String respDemo;

    @TableField("create_time")
    private Date createTime;

    @TableField("modify_time")
    private Date modifyTime;

}
