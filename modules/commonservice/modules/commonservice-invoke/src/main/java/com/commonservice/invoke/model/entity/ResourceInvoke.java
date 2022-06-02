package com.commonservice.invoke.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("t_resource_invoke")
@Data
public class ResourceInvoke implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("resource_id")
    private Long resourceId;

    @TableField("resource_name")
    private String resourceName;

    @TableField("resource_link")
    private String resourceLink;

    @TableField("http_method")
    private String httpMethod;

    @TableField("content_type")
    private String contentType;

    @TableField("req_header")
    private String reqHeader;

    @TableField("req_data")
    private String reqData;

    @TableField("resp_code")
    private Integer respCode;

    @TableField("resp_data")
    private String respData;

    @TableField("is_success")
    private Integer isSuccess;

    @TableField("error_message")
    private String errorMessage;

    @TableField("invoke_time")
    private Date invokeTime;

    @TableField("return_time")
    private Date returnTime;

    @TableField("cost")
    private Long cost;

    @TableField("create_time")
    private Date createTime;

    @TableField("modify_time")
    private Date modifyTime;

}