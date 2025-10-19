package com.ground.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ground.core.callback.IPreparedStatementSetter;
import com.ground.core.callback.IRowMapper;
import com.ground.core.callback.RowMapperResultSetExtractorImpl;
import com.ground.core.datasource.DBEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericMutilDBHelper
{
	private static Logger m_logger = LoggerFactory.getLogger(GenericMutilDBHelper.class);
	
	private static final int THREAD_SIZE = 10;
	
	private static ExecutorService excute = Executors
		.newFixedThreadPool(THREAD_SIZE);
	
	public static int count(DBEnv db, String sql,
		IPreparedStatementSetter setter)
		throws Exception
	{
		int size = db.indexSize();
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
        for(int dbIndex = 0; dbIndex < size; dbIndex++)
        {
            DBCount sl = new DBCount(db, dbIndex, sql, setter);
            list.add(excute.submit(sl));
        }
        int ret = 0;
        for(Future<Integer> f : list)
        {
            int or = f.get();
            ret += or;
        }

        return ret;
		
	}
	
	public static <T> List<T> queryCollection(DBEnv db, String sql,
		IPreparedStatementSetter setter, IRowMapper<T> rowMapper)
		throws Exception
	{
		int size = db.indexSize();
		List<Future<List<T>>> list = new ArrayList<Future<List<T>>>();
        for(int dBIndex = 0; dBIndex < size; dBIndex++)
        {
            DBSelect<T> sl = new DBSelect<T>(db, dBIndex, sql, setter, rowMapper);
            list.add(excute.submit(sl));
            m_logger.debug("excute db " + dBIndex);
        }
        List<T> ret = new ArrayList<T>();
        for(Future<List<T>> f : list)
        {
            List<T> or = f.get();
            ret.addAll(or);
        }

        return ret;

	}
	
	public static int delete(DBEnv db, String sql,
		IPreparedStatementSetter setter)
		throws Exception
	{
		int size = db.indexSize();
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
        for(int dbIndex = 0; dbIndex < size; dbIndex++)
        {
            DBDelete sl = new DBDelete(db, dbIndex, sql, setter);
            list.add(excute.submit(sl));
        }
        int ret = 0;
        for(Future<Integer> f : list)
        {
            int or = f.get();
            ret += or;
        }

        return ret;

	}
	
	private static class DBSelect<T> implements Callable<List<T>>
	{
		private DBEnv db;
		
		private int dbIndex;
		
		private String sql;
		
		private IPreparedStatementSetter setter;
		
		private IRowMapper<T> rowMapper;
		
		public DBSelect(DBEnv db, int dbIndex, String sql,
			IPreparedStatementSetter setter, IRowMapper<T> rowMapper)
		{
			this.db = db;
			this.dbIndex = dbIndex;
			this.sql = sql;
			this.setter = setter;
			this.rowMapper = rowMapper;
		}
		
		@Override
		public List<T> call()
			throws Exception
		{
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try
			{
				conn = db.borrowConn(dbIndex);
				st = conn.prepareStatement(sql);
				if(setter != null)
					setter.setValues(st);
				rs = st.executeQuery();
				List<T> result = new RowMapperResultSetExtractorImpl<T>(
					rowMapper).extractData(rs);
				return result;
			}
			finally
			{
				db.returnConn(dbIndex, conn);
				if(null != rs)
					rs.close();
				if(null != st)
					st.close();
			}
		}
		
	}
	
	private static class DBDelete implements Callable<Integer>
	{
		private DBEnv db;
		
		private int dbIndex;
		
		private String sql;
		
		private IPreparedStatementSetter setter;
		
		public DBDelete(DBEnv db, int dbIndex, String sql,
			IPreparedStatementSetter setter)
		{
			this.db = db;
			this.dbIndex = dbIndex;
			this.sql = sql;
			this.setter = setter;
		}
		
		@Override
		public Integer call()
			throws Exception
		{
			Connection conn = null;
			PreparedStatement st = null;
			try
			{
				conn = db.borrowConn(dbIndex);
				st = conn.prepareStatement(sql);
				if(setter != null)
					setter.setValues(st);
				int num = st.executeUpdate();
				return num;
			}
			finally
			{
				db.returnConn(dbIndex, conn);
				if(null != st)
					st.close();
			}
		}
	}
	
	private static class DBCount implements Callable<Integer>
	{
		private DBEnv db;
		
		private int dbIndex;
		
		private String sql;
		
		private IPreparedStatementSetter setter;
		
		public DBCount(DBEnv db, int dbIndex, String sql,
			IPreparedStatementSetter setter)
		{
			this.db = db;
			this.dbIndex = dbIndex;
			this.sql = sql;
			this.setter = setter;
		}
		
		@Override
		public Integer call()
			throws Exception
		{
			Connection conn = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try
			{
				conn = db.borrowConn(dbIndex);
				st = conn.prepareStatement(sql);
				if(setter != null)
					setter.setValues(st);
				rs = st.executeQuery();
				if(rs == null || !rs.next())
					return 0;
				return rs.getInt(1);
			}
			finally
			{
				db.returnConn(dbIndex, conn);
				if(null != rs)
					rs.close();
				if(null != st)
					st.close();
			}
		}
	}
}