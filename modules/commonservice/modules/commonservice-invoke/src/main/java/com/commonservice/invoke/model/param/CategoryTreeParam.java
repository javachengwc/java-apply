package com.commonservice.invoke.model.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryTreeParam implements Serializable {

    private Long id;

    private String name;

    private Long parentId;

}
