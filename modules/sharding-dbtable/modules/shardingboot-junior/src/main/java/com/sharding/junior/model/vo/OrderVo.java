package com.sharding.junior.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderVo implements Serializable {

    private Long id;

    private String orderNo;

    private Long userId;

    private String userName;

    private Integer statu;

    private Long price;

    private Long shopId;

    private String shopName;

}
