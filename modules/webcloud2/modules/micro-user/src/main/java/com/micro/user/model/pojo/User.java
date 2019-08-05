package com.micro.user.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.model.base.BasePojo;
import lombok.Data;

import java.util.Date;

@TableName("user")
@Data
public class User extends BasePojo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField("nick_name")
    private String nickName;

    private Integer sex;

    private String mobile;

    private String passwd;

    private String salt;

    //用户状态 0--正常 1--禁用
    private Integer statu;

    //注册来源 1--pc 2--app
    @TableField("reg_comefrom")
    private Integer regComefrom;

    @TableField("reg_time")
    private Date regTime;

    //最后登录的登录记录id
    @TableField("last_login_id")
    private Long lastLoginId;

    //最后登陆时间
    @TableField("last_login_time")
    private Date lastLoginTime;

    @TableField("create_time")
    private Date createTime;

    @TableField("modified_time")
    private Date modifiedTime;

}