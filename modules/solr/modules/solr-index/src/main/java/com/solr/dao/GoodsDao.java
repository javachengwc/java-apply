package com.solr.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.solr.dao.callback.EntityResultSetWrapImpl;
import com.solr.dao.callback.PkStatementSetterImpl;
import com.solr.dao.callback.PropertyRsExtractorAndMapperImpl;
import com.solr.model.Goods;
import com.solr.model.base.Property;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

@Repository
public class GoodsDao extends BaseDao {
	
	private static Logger m_logger = Logger.getLogger(GoodsDao.class);
	
	public Goods getById(String id)
	{
		String query ="select id,name,description from goods where id =?";
		PreparedStatementSetter setter  = new PkStatementSetterImpl<String>(id);
		Map<String,String> map= new TreeMap<String,String>();
		map.put("id", "id");
		map.put("name", "name");
		map.put("description", "description");
		
		ResultSetExtractor<Goods> resultWrapper = new EntityResultSetWrapImpl<Goods>(Goods.class,map);
		Goods goods =this.getJdbcTemplate().query(query,setter, resultWrapper);
		
		m_logger.info("GoogsDao getById invoked,param id="+id+",result goods="+goods);
		
		return goods;
	}
	/**
	 * 获取录入后的商品信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Property getGoodsInfo(int id)
	{
		String query = "select *  from java_goods where id =?";
		PreparedStatementSetter setter  = new PkStatementSetterImpl<Integer>(id);
		ResultSetExtractor<Property> propertyWrapper = new PropertyRsExtractorAndMapperImpl();
		Property result = this.getJdbcTemplate().query(query,setter,propertyWrapper);
		
		m_logger.info("GoogsDao getGoodsInfo invoked,param id="+id+",result="+result);
		
		return result;
	}
	/**
	 * 执行存储过程，产生商品中间数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean produceGoodsInfo(final int id) throws Exception 
	{
		final String callFunctionSql = "{call java_onetime_update(?)}";
		
		List<SqlParameter> params = new ArrayList<SqlParameter>();  
	    params.add(new SqlParameter(Types.INTEGER));  
	    CallableStatementCreator creator = new CallableStatementCreator()
    		{
		    	public CallableStatement createCallableStatement(Connection conn) throws SQLException {  
		              CallableStatement cstmt = conn.prepareCall(callFunctionSql);  
		              cstmt.setInt(1, id);  
		              return cstmt;}
    		};
	    		
	    Map<String,Object> result =this.getJdbcTemplate().call(creator, params);
	    
	    m_logger.info("GoogsDao produceGoodsInfo invoked,param id="+id+",result="+result);
	    
	    return true;
	}

}
