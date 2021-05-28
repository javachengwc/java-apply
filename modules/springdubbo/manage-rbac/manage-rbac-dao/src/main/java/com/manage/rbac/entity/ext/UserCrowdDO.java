package com.manage.rbac.entity.ext;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserCrowdDO implements Serializable {

    /**关联id*/
    private Integer id;
    /**用户id*/
    private Integer userId;
    /**用户网名*/
    private String userNickname;
    /**用户组id*/
    private Integer crowdId;
    /**创建人id*/
    private Integer createrId;
    /**创建人网名*/
    private String createrNickname;
    /**创建时间*/
    private Date createTime;
    /**修改时间*/
    private Date modifyTime;

    private String crowdName;
}
