package com.commonservice.invoke.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class HttpResponse implements Serializable {

    private boolean success;

    private Integer code;

    private Object body;

    private boolean json = false;

}
