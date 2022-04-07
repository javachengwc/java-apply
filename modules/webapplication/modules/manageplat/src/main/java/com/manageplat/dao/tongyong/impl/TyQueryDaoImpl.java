package com.manageplat.dao.tongyong.impl;

import com.manageplat.dao.tongyong.TyQueryDao;
import com.manageplat.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * 通用查询访问实现类
 */
@Repository
public class TyQueryDaoImpl implements TyQueryDao{

    private static Logger logger = LoggerFactory.getLogger(TyQueryDaoImpl.class);

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 列表查询
     * @param querySql
     * @return
     */
    public List<Map<String,Object>> queryList(String querySql)
    {

        logger.info("TyQueryDaoImpl queryList querySql:\r\n"+querySql);

        Connection conn=null;
        Statement st = null;
        ResultSet rs = null;
        ResultSetMetaData metaData;

        try{
            conn =getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(querySql);
            metaData = rs.getMetaData();
            //列数
            int colCount = metaData.getColumnCount();

            List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();

            while (rs.next()) {

                Map<String,Object> map = new HashMap<String,Object>(colCount);

                for (int i=1;i<=colCount;i++) {

                    int type = metaData.getColumnType(i);
                    String colName =metaData.getColumnName(i);

                    switch (type) {
                        case Types.VARCHAR:
                        case Types.CHAR:
                        case Types.BLOB:
                        case Types.LONGNVARCHAR:
                        case Types.LONGVARCHAR:
                        case Types.NCHAR:
                        case Types.NVARCHAR:
                        case Types.VARBINARY:
                        case Types.BINARY:
                            String vlu = rs.getString(i);
                            map.put(colName,vlu);
                            break;
                        case Types.BIGINT:
                        case Types.INTEGER:
                        case Types.BIT:
                        case Types.TINYINT:
                        case Types.SMALLINT:
                            //默认0
                            Long longVlu=  rs.getLong(i);

                            //返回空，表示列值是空
                            if (rs.wasNull()) {
                                longVlu= null;
                            }
                            map.put(colName,longVlu);
                            break;
                        case Types.DECIMAL:
                        case Types.DOUBLE:
                        case Types.FLOAT:
                        case Types.NUMERIC:
                        case Types.REAL:
                            Double dVlu = rs.getDouble(i);
                            if (rs.wasNull()) {
                                dVlu = null;
                            }
                            map.put(colName,dVlu);
                            break;
                        case Types.DATE:
                            try {
                                java.util.Date date = rs.getDate(i);
                                if (rs.wasNull()) {
                                    map.put(colName,null);
                                } else {
                                    map.put(colName,new java.util.Date(date.getTime()));
                                }
                            } catch (Throwable t) {
                                map.put(colName,null);
                            }
                            break;
                        case Types.TIME:
                            try {
                                java.util.Date date = rs.getTime(i);
                                if (rs.wasNull()) {
                                    map.put(colName,null);
                                } else {
                                    map.put(colName,new java.util.Date(date.getTime()));
                                }
                            } catch (Throwable t) {
                                map.put(colName,null);
                            }
                            break;
                        case Types.TIMESTAMP:
                            try {
                                java.util.Date date = rs.getTimestamp(i);
                                if (rs.wasNull()) {
                                    map.put(colName,null);
                                } else {
                                    map.put(colName,new java.util.Date(date.getTime()));
                                }
                            } catch (Throwable t) {
                                map.put(colName,null);
                            }
                            break;
                        default:
                            logger.error("type not support " + type);
                            break;
                        }
                }
                data.add(map);
            }
            return data;
         }
        catch (Exception e)
        {

        }finally {
            DbUtil.closeResource(conn, st, rs);
        }

        return null;
    }

    /**
     * 总数
     * @param countSql
     * @return
     */
    public int count(String countSql)
    {
        logger.info("TyQueryDaoImpl queryList querySql:\r\n"+countSql);

        Connection conn=null;
        Statement st = null;
        ResultSet rs = null;

        try{
            conn =getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(countSql);
            int count=0;
            if(rs.next())
            {
                count =rs.getInt(1);
            }
            return count;
        }
        catch (Exception e)
        {
            logger.error("TyQueryDaoImpl count error",e);

        }finally {
            DbUtil.closeResource(conn, st, rs);
        }
        return 0;
    }

}
