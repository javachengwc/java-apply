package com.micro.user.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.model.base.BasePojo;
import lombok.Data;

import java.util.Date;

@TableName("user_wallet")
@Data
public class UserWallet extends BasePojo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("user_name")
    private String userName;

    @TableField("user_mobile")
    private String userMobile;

    private Long amount;

    @TableField("freeze_amount")
    private Long freezeAmount;

    @TableField("create_time")
    private Date createTime;

    @TableField("modified_time")
    private Date modifiedTime;

}