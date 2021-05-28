package com.manage.rbac.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "系统", value = "systemDTO")
public class SystemDTO extends BaseDTO {

	@ApiModelProperty("系统id")
	/**系统id*/
	private Integer id;

	@ApiModelProperty("名称")
	/**名称*/
	private String name;

	@ApiModelProperty("访问地址")
	/**访问地址*/
	private String url;

	@ApiModelProperty("状态(0--上线 1--下线)")
	/**状态(0--上线 1--下线)*/
	private Integer state;

	/**备注*/
	@ApiModelProperty("备注")
	private String remark;

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

	/**逻辑删除（1不可用，0可用）*/
	@ApiModelProperty("逻辑删除（1不可用，0可用）")
	private Integer disable; 
}
