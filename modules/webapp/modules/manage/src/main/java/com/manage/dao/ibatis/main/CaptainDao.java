package com.manage.dao.ibatis.main;

import com.manage.model.main.Captain;
import org.springframework.stereotype.Repository;

@Repository
public class CaptainDao extends MainDao<Captain, Integer> {

	@Override
	public Class<Captain> getEntityClass() {
		return Captain.class;
	}
	
}