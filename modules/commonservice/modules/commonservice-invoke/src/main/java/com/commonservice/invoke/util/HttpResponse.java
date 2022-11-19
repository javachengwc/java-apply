package com.commonservice.invoke.util;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class HttpResponse implements Serializable {

    //是否成功
    private boolean success;

    //返回code码
    private Integer code;

    //header头信息
    private Map<String,String> headers;

    //body内容
    private Object body;

    //是否json
    private boolean json = false;

    //文本类型   application/json, application/msexcel
    private String contentType;

    //是否文件
    private boolean beFile = false;

    //文件名称
    private String filename;

}
