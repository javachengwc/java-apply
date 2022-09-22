package com.commonservice.invoke.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("t_access_log")
@Data
public class AccessLog  implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("sys_id")
    private Long sysId;

    @TableField("resource_id")
    private Long resourceId;

    @TableField("resource_path")
    private String resourcePath;

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
