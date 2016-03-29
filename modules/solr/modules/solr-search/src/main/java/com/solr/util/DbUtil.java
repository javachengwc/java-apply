package com.solr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DbUtil.class);

    public static Map<String,String> getDbInfo(Connection conn) throws SQLException
    {

        Map<String,String> map =new HashMap<String,String>();
        DatabaseMetaData dbmd=conn.getMetaData(); //获取DatabaseMetaData实例
        map.put("cata",conn.getCatalog());//获取数据库目录名称
        map.put("dbName",dbmd.getDatabaseProductName()); //获取数据库名称
        map.put("version",dbmd.getDatabaseProductVersion());  //获取数据库版本号
        map.put("driver",dbmd.getDriverName());  //获取JDBC驱动器名称
        map.put("driverVersion",dbmd.getDriverVersion());  //获取驱动器版本号
        map.put("userName",dbmd.getUserName());	 //获取登录用户名

        closeResource(conn);

        return map;
    }

	private static void closeResource(Connection conn) throws SQLException {
		if (null != conn) {
			conn.close();
		}
	}

	private static void closeResource(PreparedStatement pstmt)
			throws SQLException {
		if (null != pstmt) {
			pstmt.close();
		}
	}

	private static void closeResource(ResultSet rs) throws SQLException {
		if (null != rs) {
			rs.close();
		}
	}

	public static void closeResource(Connection conn, PreparedStatement pstmt,
			ResultSet rs) {
		try {
			closeResource(rs);
			closeResource(pstmt);
			closeResource(conn);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public static void closeResource(Connection conn, PreparedStatement pstmt) {
		try {
			closeResource(pstmt);
			closeResource(conn);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public static void closeResource(PreparedStatement pstmt, ResultSet rs) {
		try {
			closeResource(rs);
			closeResource(pstmt);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
