package com.manage.rbac.entity.ext;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CrowdRoleDO implements Serializable {
    /**
     * 关联id
     */
    private Integer id;
    /**
     * 用户组id
     */
    private Integer crowdId;
    /**
     * 角色id
     */
    private Integer roleId;
    /**
     * 创建人id
     */
    private Integer createrId;
    /**
     * 创建人网名
     */
    private String createrNickname;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date modifyTime;

    private String crowdName;

    private String roleName;
}
