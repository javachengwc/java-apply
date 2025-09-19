package com.z7z8.dao.monit;

import com.z7z8.model.monit.ImageInfo;
import com.z7z8.model.monit.ProcessInfo;
import com.z7z8.model.monit.ProcessItem;
import com.z7z8.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;


@Repository
public class MonitDao {
	
	private static Logger m_logger = LoggerFactory.getLogger(MonitDao.class);
	
	private static final String INSERT_PROCESS_SQL = "INSERT INTO monit_process (user_name, machine_name, record_time) VALUES (?, ?, ?)";
	
	private static final String INSERT_PROCESS_ITEM_SQL = "INSERT INTO monit_process_item (process_id, name, title, file_name) VALUES (?, ?, ?, ?)";
	
	private static final String INSERT_IMAGE_SQL = "INSERT INTO monit_image (user_name, machine_name, record_time, file_name, path) VALUES (?, ?, ?, ?, ?)";
	
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public MonitDao()
	{
		System.out.println("MonitDao create");
	}
	
	public Connection getConnection() {
		try {
			return getDataSource().getConnection();
		} catch (SQLException e) {
			m_logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public boolean recordProcessInfo(ProcessInfo info) throws Exception
	{
		int processId =insertProcessInfo(info);
		if(processId>=0)
		{
			for(ProcessItem item: info.getItems())
			{
				item.setProcessId(processId);
				insertProcessItem(item);
			}
		}else
		{
			return false;
		}
		return true;
	}
	
	public int insertProcessInfo(ProcessInfo info) throws Exception
	{
		int rt=-1;
        System.out.println("insertProcessInfo start");
		
		Connection conn=null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			conn =getConnection();
			ps = conn.prepareStatement(INSERT_PROCESS_SQL,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, info.getUserName());
			ps.setString(2, info.getMachineName());
			// 会丢失时间数据
			//preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
			//可以这样来处理
			//preparedStatement.setTimestamp(1, new java.sql.Timestamp(new java.util.Date().getTime()));
			ps.setTimestamp(3, new Timestamp(info.getTime().getTime()));
			
			ps.executeUpdate();
			
			rs=ps.getGeneratedKeys();
            if(rs.next())
            {
                rt = rs.getInt(1);
            }
		} catch (Exception e) {
			m_logger.error("MonitDao insertProcessInfo error,", e);
		} finally {
			DbUtil.closeResource(conn, ps, rs);
		}
		return rt;
	}
	
	public int insertProcessItem(ProcessItem item) throws Exception
	{
		int rt=-1;
        System.out.println("insertProcessItem start");
		
		Connection conn=null;
		PreparedStatement ps = null;
		try {
			conn =getConnection();
			ps = conn.prepareStatement(INSERT_PROCESS_ITEM_SQL);
			ps.setInt(1, item.getProcessId());
			ps.setString(2, item.getName());
			ps.setString(3, item.getTitle());
			ps.setString(4, item.getFileName());
			rt=ps.executeUpdate();
		} catch (Exception e) {
			m_logger.error("MonitDao insertProcessItem error,", e);
		} finally {
			DbUtil.closeResource(conn, ps);
		}
		return rt;
	}
	
	public int insertImage(ImageInfo image) throws Exception {
		
		int rt=-1;
        System.out.println("insertImage start");
		
		Connection conn=null;
		PreparedStatement ps = null;
		try {
			conn =getConnection();
			ps = conn.prepareStatement(INSERT_IMAGE_SQL);
			ps.setString(1, image.getUserName());
			ps.setString(2, image.getMachineName());
			ps.setTimestamp(3, new Timestamp(image.getTime().getTime()));
			//ps.setDate(3, new Date(image.getTime().getTime()));
			ps.setString(4, image.getFileName());
			ps.setString(5, image.getPath());
			
			rt=ps.executeUpdate();
		} catch (Exception e) {
			m_logger.error("MonitDao insertImage error,", e);
		} finally {
			DbUtil.closeResource(conn, ps);
		}
		return rt;
	}

}