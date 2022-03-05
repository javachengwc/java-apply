package com.es.consumer.es;

/**
 * 写入es模式
 */
public enum EsWriteModeEnum {

    SINGLE("single","单个"),
    BULK("bukl","批量"),
    HALF("half","半批量"); //当批量线程有空闲时就批量，否则单个处理

    private String code;

    private String name;

    EsWriteModeEnum(String code,String name) {
        this.code=code;
        this.name=name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
