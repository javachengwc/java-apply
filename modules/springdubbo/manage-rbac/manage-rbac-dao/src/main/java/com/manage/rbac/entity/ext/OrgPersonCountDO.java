package com.manage.rbac.entity.ext;

import lombok.Data;

import java.io.Serializable;

//机构人数
@Data
public class OrgPersonCountDO implements Serializable {

    private Integer orgId;

    private Long count;

}
