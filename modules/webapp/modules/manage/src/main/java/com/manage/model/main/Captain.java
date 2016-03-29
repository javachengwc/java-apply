package com.manage.model.main;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "tdgame_v_captain")
public class Captain {
	
	private Integer code;
	
	private Integer sex;
	
	private Integer capforce;
	
	private Integer job;
	
	private String capname;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getCapforce() {
		return capforce;
	}

	public void setCapforce(Integer capforce) {
		this.capforce = capforce;
	}

	public Integer getJob() {
		return job;
	}

	public void setJob(Integer job) {
		this.job = job;
	}

	public String getCapname() {
		return capname;
	}

	public void setCapname(String capname) {
		this.capname = capname;
	}

}
