package com.manage.rbac.entity.ext;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeacherDO implements Serializable {

    private Integer id;

    private Long uid;

    private String account;

    private String mobile;

    private String name;

    private String nickname;

    private Integer state;

    private Integer disable;

    private Integer orgId;

    private String orgName;
}
