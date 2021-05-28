package com.manage.rbac.model.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class OperateLogDTO implements Serializable {
	/***/
	private Long id; 
	/**动作*/
	private String action; 
	/**系统id*/
	private Integer systemId; 
	/**系统名称*/
	private String systemName; 
	/**功能模块*/
	private String module; 
	/**详情*/
	private String detail; 
	/**创建人id*/
	private Integer createrId; 
	/**创建人网名*/
	private String createrNickname; 
	/**创建时间*/
	private Date createTime;
	/**修改时间*/
	private Date modifyTime;
}
