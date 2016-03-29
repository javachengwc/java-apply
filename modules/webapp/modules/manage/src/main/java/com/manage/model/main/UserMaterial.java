package com.manage.model.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.manage.util.Constants;

public class UserMaterial {
	
	private String id;
	
	private String owner;
	
	private int materialid;
	
	private String name;
	
	private String playerName;
	
	private long createTime;
	
	private int count;
	
	private int state;
	
	private int place;
	
	private String memo;
	
	private Integer matelType;
	
	private MaterialExt material;
	
	public UserMaterial()
	{
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getMaterialid() {
		return materialid;
	}

	public void setMaterialid(int materialid) {
		this.materialid = materialid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public MaterialExt getMaterial() {
		return material;
	}

	public void setMaterial(MaterialExt material) {
		this.material = material;
	}
	
	public Integer getMatelType() {
		return matelType;
	}

	public void setMatelType(Integer matelType) {
		this.matelType = matelType;
		if(this.material==null)
			material=new MaterialExt();
		material.setMatelType(matelType);
	}

	public String getCreateTimeStr()
	{
		if(this.createTime==0 )
		{
			return null;
		}
		Date date =new Date(this.createTime);
		DateFormat format=new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
		return format.format(date);
	}

	
}
