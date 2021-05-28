package com.manage.rbac.model.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户组", value = "crowdDTO")
public class CrowdDTO extends BaseDTO {

	/**用户组id*/
	@ApiModelProperty("用户组id")
	private Integer id;

	/**名称*/
	@ApiModelProperty("名称")
	private String name;

	/**状态，0:正常、1:禁用，默认值:0*/
	@ApiModelProperty("状态，0:正常、1:禁用")
	private Integer state;

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

	@ApiModelProperty("机构列表")
	private List<OrganizationDTO> orgList= Collections.EMPTY_LIST;

	@ApiModelProperty("角色列表")
	private List<RoleDTO> roleList= Collections.EMPTY_LIST;
}
