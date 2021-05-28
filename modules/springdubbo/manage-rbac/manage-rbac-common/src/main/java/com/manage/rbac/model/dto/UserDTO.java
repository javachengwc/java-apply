package com.manage.rbac.model.dto;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户", value = "userDTO")
public class UserDTO extends BaseDTO {

	/**账号id*/
	@ApiModelProperty("用户id")
	private Integer id; 

	/**平台账号id*/
	@ApiModelProperty("平台账号id")
	private Long uid;

	/**平台账号*/
	@ApiModelProperty("平台账号")
	private String account;

	/**平台账号昵称*/
	@ApiModelProperty("平台账号昵称")
	private String nick;

	/**手机号*/
	@ApiModelProperty("手机号")
	private String mobile;

	/**姓名*/
	@ApiModelProperty("姓名")
	private String name;

	/**网名*/
	@ApiModelProperty("网名")
	private String nickname;

	/**密码*/
	@ApiModelProperty("密码")
	@JsonIgnore
	private String passwd;

	/**状态，0:正常、1:禁用，默认值:0*/
	@ApiModelProperty("状态，0:正常、1:禁用")
	private Integer state;

	/**上级id*/
	@ApiModelProperty("上级id")
	private Integer superiorId;

	/**上级姓名*/
	@ApiModelProperty("上级姓名")
	private String superiorName;

	/**上级网名*/
	@ApiModelProperty("上级网名")
	private String superiorNickname;

	/**机构id*/
	@ApiModelProperty("机构id")
	private Integer orgId;

	/**机构名称*/
	@ApiModelProperty("机构名称")
	private String orgName;

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

	/**逻辑删除（1删除，0不删除）*/
	@ApiModelProperty("逻辑删除（1删除，0不删除）")
	private Integer disable;

	@ApiModelProperty("岗位列表")
	private List<PostDTO> postList= Collections.EMPTY_LIST;

	@ApiModelProperty("用户组列表")
	private List<CrowdDTO> crowdList= Collections.EMPTY_LIST;
}
