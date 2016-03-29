package com.solr.web.action;

import com.solr.model.Goods;
import com.solr.model.base.Property;
import com.solr.service.business.GoodsService;
import com.solr.service.solr.SolrSchemaConfig;
import com.solr.web.action.response.ExceptionResponse;
import com.solr.web.action.response.Return;
import org.apache.log4j.Logger;

import com.util.BlankUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("goods")
public class GoodsAction {

	private static Logger m_logger = Logger.getLogger(GoodsAction.class);
	
	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 商品信息更改
	 */
	@ResponseBody
	@RequestMapping("change")
	public Return changeGoods(Integer goodsId,Integer optFlag)
	{
		 m_logger.info("GoodsAction changeGoods invoked,param goodsId ="+ goodsId+",optFlag="+optFlag);
		//校验
		 if(goodsId<=0 || optFlag<=0 )
		 {
	         return new Return(-1,"参数错误");
		 }
		 try
		 {
			 goodsService.changeGoods(goodsId, optFlag);
			 return Return.Success;
		 }catch(Exception e)
		 {
			 m_logger.error("GoodsAction changeGoods invoked error."+e);
			 return new ExceptionResponse("应用异常",e);
		 }
		 
	}
	
	/**
	 * 商品库存更改
	 */
	@ResponseBody
	@RequestMapping("changeStock")
	public Return changeGoodsStock(Integer goodsId)
	{
		 m_logger.info("GoodsAction changeGoodsStock invoked,param goodsId ="+ goodsId);
		 try
		 {
			 goodsService.changeGoodsStock(goodsId);
			 return Return.Success;
		 }catch(Exception e)
		 {
			 m_logger.error("GoodsAction changeGoodsStock invoked error."+e);
			 return new ExceptionResponse("应用异常",e);
		 }
	}
	
	/**
	 * 商品库存更改
	 */
	@ResponseBody
	@RequestMapping("changeStore")
	public Return changeGoodsStore(Integer goodsId,String allocateCity,String spotCity )
	{
		 m_logger.info("GoodsAction changeStore invoked,param goodsId ="+ goodsId+",allocateCity="+allocateCity+",spotCity="+spotCity);
		 //校验
		 if(goodsId<=0 )
		 {
	         return new Return(-1,"参数错误");
		 }
		 //参数处理
		 if(BlankUtil.isBlank(allocateCity) || ",0".equals(allocateCity) || ",0,".equals(allocateCity))
		 {
			 allocateCity="0";
		 }
		 if(BlankUtil.isBlank(spotCity) || ",0".equals(spotCity) || ",0,".equals(spotCity))
		 {
			 spotCity="0";
		 }
		 if(!"0".equals(allocateCity))
		 {
			 if(!allocateCity.startsWith(","))
			 {
				 allocateCity=","+allocateCity;
			 }
			 if(!allocateCity.endsWith(","))
			 {
				 allocateCity=allocateCity+",";
			 }
		 }
		 if(!"0".equals(spotCity))
		 {
			 if(!spotCity.startsWith(","))
			 {
				 spotCity=","+spotCity;
			 }
			 if(!spotCity.endsWith(","))
			 {
				 spotCity=spotCity+",";
			 }
		 }
		 m_logger.info("GoodsAction changeStore invoked after deal param, goodsId ="+ goodsId+",allocateCity="+allocateCity+",spotCity="+spotCity);
		 try
		 {
			 goodsService.changeGoodsStore(goodsId,allocateCity,spotCity);
			 return Return.Success;
		 }catch(Exception e)
		 {
			 m_logger.error("GoodsAction changeStore invoked error."+e);
			 return new ExceptionResponse("应用异常",e);
		 }
		 
	}
	
	@ResponseBody
	@RequestMapping("test")
	public Return testGoods(String goodsId,Integer optFlag) {
		
		m_logger.info("GoodsAction testGoods invoked,param goodsId ="+ goodsId+",optFlag="+optFlag);
		return new Return(1, "测试");
	}
	
	@ResponseBody
	@RequestMapping("query")
	public Goods queryGoods(String goodsId) {
		
		m_logger.info("GoodsAction queryGoods invoked,param goodsId ="+ goodsId);
		
		return goodsService.getGoods(goodsId);
	}
	
	/**
	 * 获取商品信息
	 */
	@ResponseBody
	@RequestMapping("info")
	public JSONObject queryGoodsInfo(Integer goodsId) {
		
		m_logger.info("GoodsAction queryGoodsInfo invoked,param goodsId ="+ goodsId);
		
		Property p = goodsService.getGoodsInfo(goodsId);
		return p.toJSONObject();
	}

	/**
	 * 获取商品信息
	 */
	@ResponseBody
	@RequestMapping("reloadSchema")
	public Return reloadSchema() {
		
		m_logger.info("GoodsAction reloadSchema invoked");
		
		SolrSchemaConfig.init();
		
		return Return.Success;
	}
}
