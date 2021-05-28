package com.manage.rbac.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class OperatorDTO implements Serializable {

    protected Integer operatorId;

    protected String operatorNickname;

    public OperatorDTO() {

    }

}
