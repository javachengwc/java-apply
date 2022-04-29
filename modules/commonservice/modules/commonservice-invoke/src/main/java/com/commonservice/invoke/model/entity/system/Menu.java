package com.commonservice.invoke.model.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TableName("t_menu")
@Data
public class Menu implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("system_id")
    private Integer systemId;

    @TableField("parent_id")
    private Integer parentId;

    /**
     * 状态，0:正常、1:禁用，默认值:0
     */
    @TableField("status")
    private Integer status;

    /**
     * 是否显示(1--显示 0--隐藏)
     */
    @TableField("visible")
    private Integer visible;

    @TableField("level")
    private Integer level;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 类型(0--目录、1--菜单、2--按钮)
     */
    @TableField("type")
    private Integer type;

    /**
     * 路由地址
     */
    @TableField("path")
    private String path;

    /**
     * 组件路径
     */
    @TableField("component")
    private String component;

    /**
     * 路由参数
     */
    @TableField("query")
    private String query;

    /**
     * 权限标识
     */
    @TableField("perms")
    private String perms;

    /**
     * 是否为外链(0--否,1--是)
     */
    @TableField("out_link")
    private Integer outLink;

    @TableField("sort")
    private Integer sort;

    @TableField("create_time")
    private Date createTime;

    @TableField("modify_time")
    private Date modifyTime;

    @TableField(exist = false)
    private List<Menu> children = new ArrayList<Menu>();

}
