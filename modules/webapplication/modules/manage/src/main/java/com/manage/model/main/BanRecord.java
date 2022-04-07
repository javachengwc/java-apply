package com.manage.model.main;

import java.util.Date;

/**
 * 封号记录
 */
public class BanRecord {

	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 被封号ID
	 */
	private String banplayer;
	/**
	 * 封号时间
	 */
	private Date bantime;
	/**
	 * 封号时长 -1为永久
	 */
	private int banday;
	/**
	 * 操作者Id
	 */
	private Integer operid;
	/**
	 * 操作人名字
	 */
	private String opername;
	
	/**
	 * 封号原因
	 */
	private String reason;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBanplayer() {
		return banplayer;
	}
	public void setBanplayer(String banplayer) {
		this.banplayer = banplayer;
	}
	public Date getBantime() {
		return bantime;
	}
	public void setBantime(Date bantime) {
		this.bantime = bantime;
	}
	public int getBanday() {
		return banday;
	}
	public void setBanday(int banday) {
		this.banday = banday;
	}
	public Integer getOperid() {
		return operid;
	}
	public void setOperid(Integer operid) {
		this.operid = operid;
	}
	public String getOpername() {
		return opername;
	}
	public void setOpername(String opername) {
		this.opername = opername;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}
