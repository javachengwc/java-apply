package com.manage.model.main.query;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.manage.model.BaseQuery;
import com.manage.model.main.UserMaterial;
import com.manage.util.Constants;

public class UserMaterialQuery extends BaseQuery implements Serializable {
	
	private static final long serialVersionUID = 3123879876855923L;
	
	private UserMaterial userMaterial=new UserMaterial();
	
	/**
	 * 创建开始时间
	 */
	private Date createTimeFrom;
	
	/**
	 * 创建结束时间
	 */
	private Date createTimeTo;
	

	public UserMaterial getUserMaterial() {
		return userMaterial;
	}

	public void setUserMaterial(UserMaterial userMaterial) {
		this.userMaterial = userMaterial;
	}
	
	
	public String getId() {
		return userMaterial.getId();
	}

	public void setId(String id) {
		userMaterial.setId(id);
	}

	public String getOwner() {
		return userMaterial.getOwner();
	}

	public void setOwner(String owner) {
		userMaterial.setOwner(owner);
	}

	public int getMaterialid() {
		return userMaterial.getMaterialid();
	}

	public void setMaterialid(int materialid) {
		userMaterial.setMaterialid(materialid);
	}

	public String getName() {
		return userMaterial.getName();
	}

	public void setName(String name) {
		userMaterial.setName(name);
	}

	public long getCreateTime() {
		return userMaterial.getCreateTime();
	}

	public void setCreateTime(long createTime) {
		userMaterial.setCreateTime(createTime);
	}


	public int getState() {
		return userMaterial.getState();
	}

	public void setState(int state) {
		userMaterial.setState(state);
	}

	public int getPlace() {
		return userMaterial.getPlace();
	}

	public void setPlace(int place) {
		userMaterial.setPlace(place);
	}
	
	public Integer getMatelType() {
		return userMaterial.getMatelType();
	}

	public void setMatelType(Integer matelType) {
		userMaterial.setMatelType(matelType);
	}

	public Date getCreateTimeFrom() {
		return createTimeFrom;
	}

	public void setCreateTimeFrom(Date createTimeFrom) {
		this.createTimeFrom = createTimeFrom;
	}

	public Date getCreateTimeTo() {
		return createTimeTo;
	}

	public void setCreateTimeTo(Date createTimeTo) {
		this.createTimeTo = createTimeTo;
	}

	public Long getCreateTimeFromLong()
	{
		if(createTimeFrom==null)
			return null;
		else
			return createTimeFrom.getTime();
	}
	public Long getCreateTimeToLong()
	{
		if(createTimeTo==null)
			return null;
		else
			return createTimeTo.getTime();
	}
	
	public String getCreateTimeFromStr()
	{
		if(createTimeFrom==null)
			return null;
		DateFormat format=new SimpleDateFormat(Constants.DATE_FORMAT);
		return format.format(createTimeFrom);
	}
	
	public String getCreateTimeToStr()
	{
		if(createTimeTo==null)
			return null;
		DateFormat format=new SimpleDateFormat(Constants.DATE_FORMAT);
		return format.format(createTimeTo);
	}

}
