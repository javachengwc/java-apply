package com.manage.service.main.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manage.dao.EntityDao;
import com.manage.dao.ibatis.main.CaptainDao;
import com.manage.model.main.Captain;
import com.manage.service.BaseService;
import com.manage.service.main.CaptainService;

@Service
public class CaptainServiceImpl extends BaseService<Captain,Integer> implements CaptainService{

	@Autowired
	private CaptainDao captainDao;

	public CaptainDao getCaptainDao() {
		return captainDao;
	}

	public void setCaptainDao(CaptainDao captainDao) {
		this.captainDao = captainDao;
	}

	@Override
	protected EntityDao<Captain, Integer> getEntityDao() {
		return captainDao;
	}

}
