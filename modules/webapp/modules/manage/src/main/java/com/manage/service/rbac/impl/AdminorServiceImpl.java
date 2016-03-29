package com.manage.service.rbac.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.EntityDao;
import com.manage.dao.ibatis.rbac.AdmRelaRoleDao;
import com.manage.dao.ibatis.rbac.AdminorDao;
import com.manage.model.dto.RoleDto;
import com.manage.model.main.BanRecord;
import com.manage.model.rbac.Adminor;
import com.manage.model.rbac.Role;
import com.manage.model.rbac.query.AdminorQuery;
import com.manage.service.BaseService;
import com.manage.service.ServiceFactory;
import com.manage.service.exception.OverTimeException;
import com.manage.service.rbac.AdminorService;
import com.manage.service.rbac.RoleService;

@Service
public class AdminorServiceImpl extends BaseService<Adminor,Integer> implements AdminorService {
	
	@Autowired
	private AdminorDao adminorDao;
	
	@Autowired
	private AdmRelaRoleDao admRelaRoleDao;
	
	@Autowired
	private RoleService roleService;

	public AdminorDao getAdminorDao() {
		return adminorDao;
	}

	public void setAdminorDao(AdminorDao adminorDao) {
		this.adminorDao = adminorDao;
	}

	public AdmRelaRoleDao getAdmRelaRoleDao() {
		return admRelaRoleDao;
	}

	public void setAdmRelaRoleDao(AdmRelaRoleDao admRelaRoleDao) {
		this.admRelaRoleDao = admRelaRoleDao;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	protected EntityDao<Adminor, Integer> getEntityDao() {
		return adminorDao;
	}
	
	public Page<Adminor> findPage(AdminorQuery query) {
		return adminorDao.findPage(query);
	}
	
	public Adminor getByName(String name)
	{
		return adminorDao.getByName(name);
	}
	
	@Transactional
	public void removeAdminor(Integer id)
	{
		Map<String,Integer> map =new HashMap<String,Integer>();
		map.put("userId", id);
		admRelaRoleDao.releaseRela(map);
		this.removeById(id);
		
		String aa="009b25cc-cbf7-4c99-a06b-0f47d6bc09e0";
		
		BanRecord record =new BanRecord();
		 record.setBanplayer(aa);
		 record.setBantime(new Date(System.currentTimeMillis()));
		 record.setReason(aa);
		//ServiceFactory.getPlayerService().banPlayer(record);
		throw new OverTimeException();
	}

	public List<RoleDto> getRelaRoles(Integer id)
	{
		List<RoleDto> list=new ArrayList<RoleDto>();
		
		List<Role> roles =roleService.findAll();
		
		List<Role> relas=roleService.getAdminorRelaRoles(id);
		
		for(Role r:roles)
		{
			if( relas!=null && relas.contains(r))
			{
				list.add(new RoleDto(r,true));
			}else
			{
				list.add(new RoleDto(r));	
			}
		}
		return list;
	}
	
	@Transactional
	public boolean assign(Adminor adminor,List<Integer> roleIds)
	{
		List<Integer> addRoles=new ArrayList<Integer>();
		List<Role> celRoles=new ArrayList<Role>();
		List<Role> relas=roleService.getAdminorRelaRoles(adminor.getId());
		for(Role role:relas)
		{
			if(!roleIds.contains(role.getId()))
				celRoles.add(role);
		}
		for(Integer roleId:roleIds)
		{
			boolean add=true;
			for(Role r:relas)
			{
				if(r.getId().intValue()==roleId)
				{
					add=false;
					break;
				}
			}
			if(add)
				addRoles.add(roleId);
		}
		try{
			
            for(Integer roleId:addRoles)
            {
            	adminorDao.addRole(adminor.getId(),roleId);
            }
            for(Role role:celRoles)
            {
            	adminorDao.cancelRole(adminor.getId(),role.getId());
            }
			return true;
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
}
