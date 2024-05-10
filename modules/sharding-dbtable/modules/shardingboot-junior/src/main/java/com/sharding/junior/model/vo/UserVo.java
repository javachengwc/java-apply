package com.sharding.junior.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVo implements Serializable {

    private Long id;

    private String userName;

}
