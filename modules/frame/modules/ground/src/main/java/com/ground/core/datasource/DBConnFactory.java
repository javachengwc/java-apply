package com.ground.core.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.log4j.Logger;

public class DBConnFactory extends BasePoolableObjectFactory
{
	private static Logger m_logger = Logger.getLogger(DBConnFactory.class);
	
	private String m_url;
	
	public DBConnFactory(String url)
	{
		m_url = url;
	}
	
	@Override
	public void destroyObject(Object o)
		throws Exception
	{
		if(o instanceof Connection)
		{
			try
			{
				Connection ss = (Connection)o;
				ss.close();
			}
			catch(Exception ex)
			{
				m_logger.error("Destroy object failed", ex);
			}
		}
		else
			throw new Exception("Unknow object");
	}
	
	@Override
	public Object makeObject()
		throws Exception
	{
		// System.out.println("创建连接了。。。。。"+m_url);
		long stat = System.currentTimeMillis();
		Object ret = DriverManager.getConnection(m_url);
		m_logger.debug("db create connection time="
			+ (System.currentTimeMillis() - stat));
		return ret;
	}
	
	@Override
	public boolean validateObject(Object o)
	{
		if(o instanceof Connection)
        {
            try {
                m_logger.debug("conn state is: " + ((Connection) o).isClosed() + ", object is: " + o);
                boolean liveConn = isLiveConn((Connection) o);
                if (!liveConn) {
                    m_logger.debug("conn disconnected with remote server");
                }
                return liveConn;
            } catch (SQLException e) {
                e.printStackTrace();
                m_logger.error("validate object failed", e);
            }
        }
        return false;
    }

    private boolean isLiveConn(Connection con) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean flag = true;
        try {
            st = con.prepareStatement("select 1");
            rs = st.executeQuery();
            int result = -1;
            if (rs.next()) {
                result = rs.getInt(1);
            }
            m_logger.debug("test result is: " + result);
            if (result != 1) {
                flag = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (null != st) {
                st.close();
            }
            if (null != rs) {
                rs.close();
            }
            return flag;
        }
    }
}