package com.z7z8.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DbUtil.class);

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
