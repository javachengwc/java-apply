package com.manage.rbac.entity.ext;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PostRoleDO implements Serializable {

    /**关联id*/
    private Integer id;
    /**岗位id*/
    private Integer postId;
    /**角色id*/
    private Integer roleId;
    /**创建人id*/
    private Integer createrId;
    /**创建人网名*/
    private String createrNickname;
    /**创建时间*/
    private Date createTime;
    /**修改时间*/
    private Date modifyTime;

    private String postName;

    private String roleName;
}