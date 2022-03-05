package com.es.consumer.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_test")
public class Test implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Date createTime;

    private Date modifyTime;
}
