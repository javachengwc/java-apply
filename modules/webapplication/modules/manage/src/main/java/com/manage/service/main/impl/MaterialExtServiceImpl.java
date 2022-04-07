package com.manage.service.main.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.org.rapid_framework.page.Page;

import com.manage.dao.EntityDao;
import com.manage.dao.ibatis.main.MaterialExtDao;
import com.manage.model.main.MaterialExt;
import com.manage.model.main.query.MaterialExtQuery;
import com.manage.service.BaseService;
import com.manage.service.ServiceFactory;
import com.manage.service.main.MaterialExtService;
import com.manage.service.main.MaterialOp;

@Service
public class MaterialExtServiceImpl extends BaseService<MaterialExt,String> implements MaterialExtService {

	@Autowired
	private MaterialExtDao materialExtDao;

	
	public MaterialExtDao getMaterialExtDao() {
		return materialExtDao;
	}

	public void setMaterialExtDao(MaterialExtDao materialExtDao) {
		this.materialExtDao = materialExtDao;
	}

	@Override
	protected EntityDao<MaterialExt, String> getEntityDao() {
		return materialExtDao;
	}
	
	public Page<MaterialExt> findPage(MaterialExtQuery query) {
		return materialExtDao.findPage(query);
	}

	@Transactional
	public boolean presentMaterial(String playerId,String combinid,int count)
	{
		String idStrs[]=combinid.split(MaterialExt.IDSPIDT);
		int ids[]= new int[idStrs.length];
		for(int i=0;i<idStrs.length;i++)
		{
			ids[i]=Integer.parseInt(idStrs[i]);
		}
		//卡牌
		if(ids[0]==MaterialExt.MaType.Card.getValue())
		{
			 return presentCard(playerId,ids[1],count);
		}
		else
		{
			//其他暂不处理
			return false;
		}
	    
	}
	@Transactional
	public boolean presentCard(String playerId,int cardid,int count)
	{
		try{
			return true;
		}catch(Exception e)
		{
			e.printStackTrace(System.out);
			return false;
			
		}
	}
}