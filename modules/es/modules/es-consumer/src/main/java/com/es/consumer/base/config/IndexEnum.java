package com.es.consumer.base.config;

public enum IndexEnum {

    ACTIVITY("activitylog","activitylog","活动索引"),
    ACCESS("accesslog","accesslog","访问日志索引");

    private String index;

    private String type;

    private String name;

    IndexEnum(String index,String type,String name) {
        this.index=index;
        this.type=type;
        this.name =name;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
