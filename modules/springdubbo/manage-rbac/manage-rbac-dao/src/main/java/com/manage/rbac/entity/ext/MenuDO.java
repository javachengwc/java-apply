package com.manage.rbac.entity.ext;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MenuDO implements Serializable {

    /**菜单id*/
    private Integer id;

    /**名称*/
    private String name;

    /**系统id*/
    private Integer systemId;

    /**系统名称*/
    private String systemName;

    /**父菜单id*/
    private Integer parentId;

    /**父菜单名称*/
    private String parentName;

    /**状态，0:正常、1:禁用，默认值:0*/
    private Integer state;

    /**层级*/
    private Integer level;

    /**菜单或按钮标识*/
    private String tag;

    /**权限*/
    private String perms;

    /**图标*/
    private String icon;

    /**类型(0--目录、1--菜单、2--按钮)*/
    private Integer type;

    /**排序*/
    private Integer sort;

    /**创建人id*/
    private Integer createrId;

    /**创建人网名*/
    private String createrNickname;

    /**创建时间*/
    private Date createTime;

    /**修改时间*/
    private Date modifyTime;

    /**操作人id*/
    private Integer operatorId;

    /**操作人网名*/
    private String operatorNickname;

}