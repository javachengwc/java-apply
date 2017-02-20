package com.ground.core;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.ground.core.callback.*;
import com.ground.core.datasource.DBEnv;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class GenericDBHelper {
	private static final Logger m_logger = Logger.getLogger(GenericDBHelper.class);

	public static <T>  T query(DBEnv db, String key,
			IPreparedStatementCreator creator,IPreparedStatementSetter setter,IResultSetExtractor<T> resultSetExtractor) throws Exception
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			conn = db.borrowConn(key);
			st=creator.createPreparedStatement(conn);
			setter.setValues(st);
			rs = st.executeQuery();
			return resultSetExtractor.extractData(rs);
		}finally
		{
			db.returnConn(key, conn);
			if(null != rs)
				rs.close();
			if(null != st)
				st.close();
		}
	}
	
	public static <T>  T query(DBEnv db, String key, IPreparedStatementCreator creator,IResultSetExtractor<T> resultSetExtractor) throws Exception
	{
		String sql =creator.genericSql();
		return query(db,key,sql,null,resultSetExtractor);
	}
	
	public static <T>  T query(DBEnv db, String key, String sql,IPreparedStatementSetter setter,IResultSetExtractor<T> resultSetExtractor) throws Exception
	{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
        if (m_logger.isDebugEnabled()) {
            m_logger.debug("sql is: " + sql);
        }
        try
		{
			conn = db.borrowConn(key);
			st = conn.prepareStatement(sql);
			if(setter!=null)
			    setter.setValues(st);
			rs = st.executeQuery();
			return resultSetExtractor.extractData(rs);
		}finally
		{
			db.returnConn(key, conn);
			if(null != rs)
				rs.close();
			if(null != st)
				st.close();
		}
	}
	
	public static <T> List<T> queryCollection(DBEnv db,String key,IPreparedStatementCreator creator,IPreparedStatementSetter setter,IRowMapper<T> rowMapper) throws Exception
	{
		return query(db,key,creator,setter,new RowMapperResultSetExtractorImpl<T>(rowMapper));
	}
	
	public static <T> List<T> queryCollection(DBEnv db,String key,IPreparedStatementCreator creator,IRowMapper<T> rowMapper) throws Exception
	{
		return query(db,key,creator,new RowMapperResultSetExtractorImpl<T>(rowMapper));
	}
	
	public static <T> List<T> queryCollection(DBEnv db,String key,String sql,IPreparedStatementSetter setter,IRowMapper<T> rowMapper) throws Exception
	{
		return query(db,key,sql,setter,new RowMapperResultSetExtractorImpl<T>(rowMapper));
	}
	
	public static <T> T execute(DBEnv db,String key,IJdbcCallback<T> callback) throws Exception
	{
		Connection conn = null;
		try
		{
			conn = db.borrowConn(key);
			return callback.doInJdbc(conn);
		}
		finally
		{
			db.returnConn(key, conn);
		}
	}
	
	
	public static int insert(DBEnv db,String key,IPreparedStatementCreator creator,IPreparedStatementSetter setter)
	throws Exception
	{
		return _dealChange(db,key,creator,setter);
	}
	
	private static int _dealChange(DBEnv db,String key,IPreparedStatementCreator creator,IPreparedStatementSetter setter) throws Exception
	{
		assertKeyNotNull(key);
		Connection conn = null;
		PreparedStatement st = null;
		try
		{
			conn = db.borrowConn(key);
			st = creator.createPreparedStatement(conn);
			setter.setValues(st);
			return st.executeUpdate();
		}
		finally
		{
			//TimerLogger.endTime(sql);
			db.returnConn(key, conn);
			if(null != st)
				st.close();
		}
		
	}
	
	private static int _dealChange(DBEnv db,String key,String sql,IPreparedStatementSetter setter) throws Exception
	{
		assertKeyNotNull(key);
		Connection conn = null;
		PreparedStatement st = null;
		try {
            if (m_logger.isDebugEnabled()) {
                m_logger.debug(sql);
            }
            conn = db.borrowConn(key);
            st = conn.prepareStatement(sql);
            if (setter != null)
                setter.setValues(st);
            return st.executeUpdate();
        } finally
		{
			//TimerLogger.endTime(sql);
			db.returnConn(key, conn);
			if(null != st)
				st.close();
		}
	}
	
	public static int insert(DBEnv db,String key,IPreparedStatementCreator creator) throws Exception
	{
		String sql =creator.genericSql();
		return insert(db,key,sql);
	}
	
	public static int insert(DBEnv db,String key,String sql) throws Exception
	{
		
		return _dealChange(db,key,sql,null);
	}
	
	public static int insert(DBEnv db,String key,String sql,IPreparedStatementSetter setter) throws Exception
	{
		return _dealChange(db,key,sql,setter);
	}
	
	public static int insertReturnId(DBEnv db,String key,IPreparedStatementCreator creator,IPreparedStatementSetter setter)
			throws Exception
	{
		assertKeyNotNull(key);
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try
		{
			conn = db.borrowConn(key);
			st = creator.createPreparedStatement(conn);
			setter.setValues(st);
			st.executeUpdate();
			rs = st.getGeneratedKeys();   
			rs.next();
			int id = rs.getInt(1);
			return id;
		}
		finally
		{
			//TimerLogger.endTime(sql);
			db.returnConn(key, conn);
			if(null != st)
				st.close();
		}
	}
	
	public static Serializable insertReturnId(DBEnv db,String key,String sql,IPreparedStatementSetter setter) throws Exception
	{
		assertKeyNotNull(key);
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
        if (m_logger.isDebugEnabled()) {
            m_logger.debug("sql is: " + sql);
        }
        try
		{
			conn = db.borrowConn(key);
			st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			setter.setValues(st);
			st.executeUpdate();
			rs = st.getGeneratedKeys();   
			rs.next();
			Object obj = rs.getObject(1);
			Serializable id= (Serializable)obj;
			return id;
		}
		finally
		{
			//TimerLogger.endTime(sql);
			db.returnConn(key, conn);
			if(null != st)
				st.close();
		}
	}
	
	public static int update(DBEnv db, String key,IPreparedStatementCreator creator,IPreparedStatementSetter setter)
		throws Exception
	{
		return _dealChange(db,key,creator,setter);
	}
	
	public static int update(DBEnv db, String key,IPreparedStatementCreator creator) throws Exception
	{
		String sql =creator.genericSql();
		return update(db,key,sql);
	}
	
	public static int update(DBEnv db, String key,String sql) throws Exception
	{
		return _dealChange(db,key,sql,null);
	}
	
	public static int update(DBEnv db, String key,String sql,IPreparedStatementSetter setter) throws Exception
	{
		return _dealChange(db,key,sql,setter);
	}
	
	public static int updateOrInsert(DBEnv db,String key,IPreparedStatementCreator updateCreator,IPreparedStatementSetter updateSetter,
			IPreparedStatementCreator insertCreator,IPreparedStatementSetter insertSetter)
		throws Exception
	{
		assertKeyNotNull(key);
		int count = update(db,key, updateCreator, updateSetter);
		if(count == 0)
		{
			count = insert(db, key, insertCreator,insertSetter);
		}
		return count;
	}
	
	public static int updateOrInsert(DBEnv db, String key, IPreparedStatementCreator updateCreator, IPreparedStatementCreator insertCreator) throws Exception
	{
		String updateStr=updateCreator.genericSql();
		String insertStr=insertCreator.genericSql();
		return updateOrInsert(db,key,updateStr,insertStr);
	}
	
	public static int updateOrInsert(DBEnv db, String key,String updateSql,String insertSql) throws Exception
	{
		assertKeyNotNull(key);
		int count = update(db,key, updateSql);
		if(count == 0)
		{
			count = insert(db, key, insertSql);
		}
		return count;
	}
	
	public static int updateOrInsert(DBEnv db, String key,String updateSql,IPreparedStatementSetter updateSetter, String insertSql,
			IPreparedStatementSetter insertSetter) throws Exception
	{
		assertKeyNotNull(key);
		int count = update(db,key, updateSql,updateSetter);
		if(count == 0)
		{
			count = insert(db, key, insertSql,insertSetter);
		}
		return count;
	}
	/**
	 * 先insert，如果发现有记录则update
	 */
	public static int insertOrUpdate(DBEnv db,String key,IPreparedStatementCreator updateCreator,IPreparedStatementSetter updateSetter,
			IPreparedStatementCreator insertCreator,IPreparedStatementSetter insertSetter)
		throws Exception
	{
		assertKeyNotNull(key);
		try
		{
			return insert(db, key,insertCreator,insertSetter);
		}catch(SQLException ex)
		{
			if(isDuplicate(ex))
				return update(db, key,updateCreator,updateSetter);
			else
				throw ex;
		}
	}
	
	public static int delete(DBEnv db, String key,IPreparedStatementCreator creator,IPreparedStatementSetter setter)
		throws Exception
	{
		return _dealChange(db,key,creator,setter);
	}
	
	public static int delete(DBEnv db, String key,IPreparedStatementCreator creator)
	throws Exception
	{
		String sql =creator.genericSql();
		return delete(db,key,sql);
	}
	
	public static int delete(DBEnv db, String key,String sql) throws Exception
	{
		return _dealChange(db,key,sql,null);
	}
    
	public static int delete(DBEnv db, String key,String sql,IPreparedStatementSetter setter) throws Exception
	{
		return _dealChange(db,key,sql,setter);
	}
	
	public static int count(DBEnv db,String key,IPreparedStatementCreator creator,IPreparedStatementSetter setter) throws Exception
	{
		Integer result= query(db,key,creator,setter,new IntegerResultSetExtractorImpl());
		if(result==null)
			result=0;
		return result;
	}
	
	public static int count(DBEnv db,String key,IPreparedStatementCreator creator) throws Exception
	{
		String sql =creator.genericSql();
		return count(db,key,sql,null);
	}
	
	public static int count(DBEnv db,String key,String sql,IPreparedStatementSetter setter) throws Exception
	{
		Integer result =query(db,key,sql,setter,new IntegerResultSetExtractorImpl());
		if(result==null)
			result =0;
		return result;
	}
	
	public static long sum(DBEnv db, String key,IPreparedStatementCreator creator,IPreparedStatementSetter setter)
		throws Exception
	{
		Long result =query(db,key,creator,setter,new LongResultSetExtractorImpl());
		if(result==null)
			result =0l;
		return result;
	}
	
	public static long sum(DBEnv db,String key,IPreparedStatementCreator creator) throws Exception
	{
		String sql =creator.genericSql();
		return sum(db,key,sql);
	}
	
	public static long sum(DBEnv db,String key,String sql) throws Exception
	{
		Long result =query(db,key,sql,null,new LongResultSetExtractorImpl());
		if(result==null)
			result =0l;
		return result;
	}
	
	public static long sum(DBEnv db,String key,String sql,IPreparedStatementSetter setter) throws Exception
	{
		Long result =query(db,key,sql,setter,new LongResultSetExtractorImpl());
		if(result==null)
			result =0l;
		return result;
	}
	
	public static void assertKeyNotNull(String key)
	{
		if(StringUtils.isBlank(key))
		{
			throw new IllegalArgumentException("the db key cannot be null");
		}
	}
	
	private static boolean isDuplicate(SQLException ex)
	{
		if(ex.getErrorCode() == 1062)
			return true;
		return false;
	}
}
