package com.manage.rbac.model.dto;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "机构", value = "organizationDTO")
public class OrganizationDTO extends BaseDTO {

	/**机构id*/
	@ApiModelProperty("机构id")
	private Integer id;

	/**机构名称*/
	@ApiModelProperty("机构名称")
	private String name;

	@ApiModelProperty("机构人数")
	private Integer personCount;

	/**机构层级*/
	@ApiModelProperty("机构层级")
	private Integer level;

	/**状态，0:正常、1:禁用，默认值:0*/
	@ApiModelProperty("状态，0:正常、1:禁用")
	private Integer state;

	/**父机构id*/
	@ApiModelProperty("父机构id")
	private Integer parentId;

	/**父机构名称*/
	@ApiModelProperty("父机构名称")
	private String parentName;

	/**机构全路径path*/
	@ApiModelProperty("机构全路径path")
	private String path;

	/**机构全路径名称*/
	@ApiModelProperty("机构全路径名称")
	private String pathName;

	/**排序*/
	@ApiModelProperty("排序")
	private Integer sort;

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

	@ApiModelProperty("岗位列表")
	private List<PostDTO> postList= Collections.EMPTY_LIST;

	@ApiModelProperty("用户组列表")
	private List<CrowdDTO> crowdList= Collections.EMPTY_LIST;
}
