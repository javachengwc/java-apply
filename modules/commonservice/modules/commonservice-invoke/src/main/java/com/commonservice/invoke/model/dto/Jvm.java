package com.commonservice.invoke.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Jvm implements Serializable {

    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK名称
     */
    private String name;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    private double used;

    private double usage;

    /**
     * 运行参数
     */
    private String inputArgs;

    /**
     * JDK启动时间
     */
    private Date startTime;

    /**
     * JDK运行时间
     */
    private String runTime;

}
