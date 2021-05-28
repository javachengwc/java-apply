package com.manage.rbac.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "角色", value = "roleDTO")
public class RoleDTO extends BaseDTO {

	/**角色id*/
	@ApiModelProperty("角色id")
	private Integer id;

	/**名称*/
	@ApiModelProperty("名称")
	private String name;

	/**编码*/
	@ApiModelProperty("编码")
	private String code;

	/**状态，0:正常、1:禁用，默认值:0*/
	@ApiModelProperty("状态，0:正常、1:禁用")
	private Integer state;

	/**角色类型(1--业务角色 2--管理角色)*/
	@ApiModelProperty("角色类型(1--业务角色 2--管理角色)")
	private Integer type;

	/**是否系统角色(0--否 1--是)*/
	@ApiModelProperty("是否系统角色(0--否 1--是)")
	private Integer sysRole;

	/**备注*/
	@ApiModelProperty("备注")
	private String remark;

	/**创建人id*/
	@ApiModelProperty("创建人id")
	private Integer createrId;

	/**创建人网名*/
	@ApiModelProperty("创建人网名")
	private String createrNickname;

	/**创建时间*/
	@ApiModelProperty("创建时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;

	/**修改时间*/
	@ApiModelProperty("修改时间")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date modifyTime;

	/**操作人id*/
	@ApiModelProperty("操作人id")
	private Integer operatorId;

	/**操作人网名*/
	@ApiModelProperty("操作人网名")
	private String operatorNickname; 
}
