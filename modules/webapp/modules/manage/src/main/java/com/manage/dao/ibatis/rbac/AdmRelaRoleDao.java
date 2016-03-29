package com.manage.dao.ibatis.rbac;

import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class AdmRelaRoleDao extends RbacDao {
	
	/**
	 * 
	 * @param map  userId:?,roleId:?
	 */
	public void releaseRela(Map<String,Integer> map)
	{
		String exeStr="delAdmRelaRole";
		
		this.getSqlMapClientTemplate().delete(exeStr, map);
	}

	@Override
	public Class getEntityClass() {
		return null;
	}

}
