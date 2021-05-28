package com.manage.rbac.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "菜单", value = "menuDTO")
public class MenuDTO extends BaseDTO {

	/**菜单id*/
	@ApiModelProperty("菜单id")
	private Integer id;

	/**名称*/
	@ApiModelProperty("名称")
	private String name;

	/**系统id*/
	@ApiModelProperty("系统id")
	private Integer systemId;

	/**父菜单id*/
	@ApiModelProperty("父菜单id")
	private Integer parentId;

	/**状态，0:正常、1:禁用，默认值:0*/
	@ApiModelProperty("状态，0:正常、1:禁用，默认值:0")
	private Integer state;

	/**层级*/
	@ApiModelProperty("层级")
	private Integer level;

	/**菜单或按钮标识*/
	@ApiModelProperty("菜单或按钮标识")
	private String tag;

	/**权限*/
	@ApiModelProperty("权限")
	private String perms;

	/**图标*/
	@ApiModelProperty("图标")
	private String icon;

	/**类型(0--目录、1--菜单、2--按钮)*/
	@ApiModelProperty("类型(0--目录、1--菜单、2--按钮)")
	private Integer type;

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

	@ApiModelProperty("系统名称")
	private String systemName;

	@ApiModelProperty("父菜单名称")
	private String parentName;

	@ApiModelProperty("是否已选择，只在分配角色菜单才用此节点 0--否 1--是")
	private Integer selected;

	@ApiModelProperty("子菜单集合")
	private List<MenuDTO> children = new ArrayList<MenuDTO>();
}
