package com.manage.dao.hibernate.rbac;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

import com.manage.dao.hibernate.BaseHibernateDao;
import com.manage.model.rbac.Module;
import com.manage.model.rbac.query.ModuleQuery;

@Repository
public class ModuleDao extends BaseHibernateDao<Module, Integer> {

	public ModuleDao()
	{
		System.out.println("///////"+ModuleDao.class.getName()+" bean craeted...");
	}
	
	@Override
	public Class<Module> getEntityClass() {
		return Module.class;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Module> getAll()
	{
		return (List<Module>)getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				StringBuffer queryString = new StringBuffer(" FROM ").append(getEntityClass().getSimpleName());
				Query query = session.createQuery(queryString.toString());
				return (List<Module>)query.list();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public Page<Module> findPage(ModuleQuery query) {
		// XsqlBuilder syntax,please see
		// http://code.google.com/p/rapid-xsqlbuilder
		// [column]为字符串拼接, {column}为使用占位符.
		// [column]为使用字符串拼接,如username='[username]',偷懒时可以使用字符串拼接
		// [column] 为PageRequest的属性
		String sql = "select t from Module t where 1=1 "
				+ "/~ and t.id = {id} ~/"
				+ "/~ and t.name = {name} ~/"
				+ "/~ and t.url = {url} ~/"
				+ "/~ and t.parent.id = {parentid} ~/"
				+ "/~ order by [sortColumns] ~/";

		//return pageQuery(sql, query);
        return null;
	}
	
}
