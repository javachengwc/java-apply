package com.commonservice.invoke.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Mem implements Serializable {

    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;


    private double usage;
}
