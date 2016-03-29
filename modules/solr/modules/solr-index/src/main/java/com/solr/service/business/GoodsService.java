package com.solr.service.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.solr.dao.GoodsDao;
import com.solr.model.Goods;
import com.solr.model.base.Property;
import com.solr.service.solr.ISolrService;
import com.solr.service.solr.SolrSchemaConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsService {
	
	private static Logger m_logger = Logger.getLogger(GoodsService.class);
	
	@Autowired
	public ISolrService solrService;
	
	@Autowired
	public GoodsDao goodsDao;

	public ISolrService getSolrService() {
		return solrService;
	}

	public void setSolrService(ISolrService solrService) {
		this.solrService = solrService;
	}
	
	public GoodsDao getGoodsDao() {
		return goodsDao;
	}

	public void setGoodsDao(GoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}
	
	/**
	 * 根据solr field 过滤得到所需要的field信息
	 * @param p
	 * @return
	 */
	public Property filterSolrFieldInfo(Property p)
	{
		m_logger.info("GoodsService filterSolrFieldInfo p.size()="+((p==null)?"0":p.size()));
		
		if(p==null || p.size()<=0)
		{
			return null;
		}
		Property rt =new Property();
		Map<String,List<String>> map = SolrSchemaConfig.getAllFields();
		List<String> fields =map.get(SolrSchemaConfig.GENERAL_FIELD);
		for(String field:fields)
		{
			if(p.containsKey(field))
			{
				rt.put(field,p.get(field));
			}else if(p.containsKey(StringUtils.upperCase(field)))
			{
				rt.put(field,p.get(StringUtils.upperCase(field)));
			}else if(p.containsKey(StringUtils.lowerCase(field)))
			{
				rt.put(field,p.get(StringUtils.lowerCase(field)));
			}
		}
		
		List<String> preFields = map.get(SolrSchemaConfig.PRE_FIELD);
		for(String field:preFields)
		{
			for(String key:p.keySet())
			{
			    if(StringUtils.lowerCase(key).startsWith(StringUtils.lowerCase(field)))
			    {
					rt.put(key.replace(key.substring(0, field.length()), field),p.get(key));
			    }
			}
		}
		List<String> sufFields = map.get(SolrSchemaConfig.SUF_FIELD);
		for(String field:sufFields)
		{
			for(String key:p.keySet())
			{
			    if(StringUtils.lowerCase(key).endsWith(StringUtils.lowerCase(field)))
			    {
			    	
			    	rt.put(key.replace(key.substring(key.length()-field.length()), field),p.get(key));
			    }
			}
		}
		//去掉空值域
		List<String> nullValuesFields =new ArrayList<String>();
		for(String key:rt.keySet())
		{
			if(rt.get(key)==null)
			{
				nullValuesFields.add(key);
			}
		}
		for(String key:nullValuesFields)
		{
			rt.remove(key);
		}
			
		m_logger.info("GoodsService filterSolrFieldInfo rt.size()="+rt==null?"0":rt.size());
		
		return rt;
	}
	
	/**
	 * 商品更新
	 * @param goodsId  商品id
	 * @param optFlag  1--增 2--改 3--删
	 */
	public boolean changeGoods(int goodsId,int optFlag) throws Exception
	{
		
		if(optFlag==1 || optFlag==2)
		{
			//调存储过程得到中间数据
			goodsDao.produceGoodsInfo(goodsId);
			
			//查询获取中间数据
			Property p = goodsDao.getGoodsInfo(goodsId);
			
			//提出solr schema需要的字段数据
			Property solrP =filterSolrFieldInfo(p);
			
			//根据中间数据更新索引
			if(solrP!=null && solrP.size()>0)
			{
				if(optFlag==1)
				{
				    int rt=solrService.addOrUpdateDocument(solrP);
				
				    m_logger.info("GoodsService changeGoods invoked,goodsId="+goodsId+",optFlag="+optFlag+",addOrUptDoc return"+rt);
				}
				if(optFlag==2)
				{
					Map<String,String> params =new TreeMap<String,String>();
					params.put("id", String.valueOf(goodsId));
				    
				    int rt= solrService.updateSpecFieldDocument(params, solrP);
				    
				    m_logger.info("GoodsService changeGoods invoked,goodsId="+goodsId+",optFlag="+optFlag+",updateSpecFieldDocument return"+rt);
				}
			}else
			{
				m_logger.info("GoodsService changeGoods invoked,goodsId="+goodsId+",optFlag="+optFlag+",未获取到最新数据");
			}
			return true;
		}
		
		if(optFlag==3)
		{
			
			//直接删除商品索引
			try{
				int rt =solrService.deleteDocument(String.valueOf(goodsId));
				
				m_logger.info("GoodsService changeGoods invoked,goodsId="+goodsId+",optFlag="+optFlag+",delDoc return"+rt);
			}catch(Exception e)
			{
				m_logger.error("GoodsService changeGoods invoked,goodsId="+goodsId+",optFlag="+optFlag+",delDoc error",e);
			}
			
			return true;
		}
		return false;
	}
	
	/**
	 * 商品库存更改
	 * @param goodsId
	 * @throws Exception
	 */
	public boolean changeGoodsStock(int goodsId) throws Exception
	{
		//这里可能还需要调存储过程 把库存的信息更新到商品表里
		//从数据库获取最新库存信息
		Property goods=goodsDao.getGoodsInfo(goodsId);
		//只更改solr索引中库存相关field信息
		if(goods!=null && goods.size()>0)
		{
			Map<String,String> params =new TreeMap<String,String>();
			params.put("id", String.valueOf(goodsId));
		    Map<String,String> specField = new TreeMap<String,String>();
		    //需要注意默认值，null值的处理
		    specField.put("allocate_city", goods.get("allocate_city"));
		    specField.put("spot_city", goods.get("spot_city"));
		    
		    int rt= solrService.updateSpecFieldDocument(params, specField);
		    
		    m_logger.info("GoodsService changeGoodsStock invoked,goodsId="+goodsId+", return"+rt);
		}
		return true;
	}
	
	/**
	 * 商品库存更改
	 * @param goodsId
	 * @param allocateCity
	 * @param spotCity
	 * @throws Exception
	 */
	public boolean changeGoodsStore(int goodsId,String allocateCity,String spotCity) throws Exception
	{
		Map<String,String> params =new TreeMap<String,String>();
		params.put("id", String.valueOf(goodsId));
	    Map<String,String> specField = new TreeMap<String,String>();
	    specField.put("allocate_city",allocateCity);
	    specField.put("spot_city",spotCity);
	    
	    int rt= solrService.updateSpecFieldDocument(params, specField);
	    
	    m_logger.info("GoodsService changeGoodsStock invoked,goodsId="+goodsId+", return"+rt);
	    
	    return true;
	}
	
	/**
	 * 获取商品信息
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	public Property getGoodsInfo(int goodsId )
	{
		return goodsDao.getGoodsInfo(goodsId);
	}
	
	public Goods getGoods(String id)
	{
		return goodsDao.getById(id);
	}

	public static void main(String args [])
	{
		String aa="ah_";
		String bb="AH_hahaha";
		String cc = "_xx";
		String dd ="c_Xx";
		
		System.out.println(bb.replace(bb.substring(0,aa.length()), aa));
		System.out.println(dd.replace(dd.substring(dd.length()-cc.length()),cc));
		
	}
}
