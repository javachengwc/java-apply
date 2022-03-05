package com.es.consumer.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TestVo  implements Serializable {

    private Long id;

    private String name;

    private Date createTime;

    private Date modifyTime;
}
