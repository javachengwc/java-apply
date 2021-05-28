package com.manage.rbac.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDTO implements Serializable {

    private OperatorDTO operator;
}
