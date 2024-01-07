package com.otd.web.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Req<T> implements Serializable {

    private T data;

    public Req() {

    }

    public Req(T data) {
        this.data=data;
    }
}
