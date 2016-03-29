package com.rule.data.service.core;

import com.alibaba.fastjson.JSON;
import com.rule.data.exception.RengineException;
import com.rule.data.model.SerColumn;
import com.rule.data.model.SerDb;
import com.rule.data.model.SerService;
import com.rule.data.model.vo.D2Data;
import com.rule.data.model.vo.RengineConnection;
import com.rule.data.render.SqlUtil;
import com.rule.data.util.ConfigUtil;
import com.rule.data.util.LogUtil;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 查询基础数据服务类
 */
public final class Cache4BaseService {

    /**
     * Mysql线程池, key是dbid, value是连接queue
     */
    public static final ConcurrentHashMap<String, ArrayBlockingQueue<RengineConnection>> CONN_POOL =new ConcurrentHashMap<String, ArrayBlockingQueue<RengineConnection>>();

    /**
     * 全局基本数据源缓存, 缓存层次是: 数据源名称-sql-d2data
     */
    public static final ConcurrentHashMap<String, ConcurrentHashMap<String, D2Data>> DATA_CACHE  = new ConcurrentHashMap<String, ConcurrentHashMap<String, D2Data>>();

    /**
     * 从db拉取的最大行数
     */
    public static final int db_row_max = ConfigUtil.getDbRowMax();

    private Cache4BaseService() {

    }

    /**
     * 基本数据源计算, 优先从缓存拿, 缓存失效或没有后, 去数据库进行查询
     *
     * @param servicePo
     * @param parameter
     * @param others
     * @return
     * @throws com.rule.data.exception.RengineException
     */
    static D2Data getD2Data(SerService servicePo, Map<String, Object> parameter, long... others) throws RengineException {

        final String dbID = servicePo.nextDbID();

        final SerDb dbPo = Services.getDB(dbID);

        final String serviceName = servicePo.getName();

        if (dbPo == null) {

            throw new RengineException(serviceName, "所需数据库" + dbID + "的连接信息未找到");
        }

        if (servicePo.getColumns().size() == 0) {

            throw new RengineException(serviceName, "列数目为0");
        }

        final int cacheTime = servicePo.getCacheTime() == null ? 0 : servicePo.getCacheTime();

        final long timestamp = servicePo.getUpdateTime() == null ? 0L : servicePo.getUpdateTime().getTime();

        final long start = System.currentTimeMillis();

        final long currentSecond = start / 1000;

        String sql = servicePo.getSql() == null ? "" : servicePo.getSql();

        if (parameter != null) {

            sql = SqlUtil.renderSql(serviceName, sql, parameter);
        }

        ConcurrentHashMap<String, D2Data> cache = null;

        //从缓存里面拿结果
        if (cacheTime > 0 && serviceName != null) {
            cache = DATA_CACHE.get(serviceName);


            if (cache == null) {
                cache = new ConcurrentHashMap<String, D2Data>();

                final ConcurrentHashMap<String, D2Data> oldCache = DATA_CACHE.putIfAbsent(serviceName, cache);

                if (oldCache != null) {
                    cache = oldCache;
                }
            }

            D2Data data = cache.get(sql);

            /**
             * 如果没有的话, 代表是正常流程, 当缓存有, 且未失效, 且与当前数据源版本一致, 则返回, 否则重新计算
             */
            if (others.length == 0) {
                if (data != null  && (currentSecond - data.createtime <= cacheTime) && data.timestamp == timestamp) {  // cache hit
                    data.lastAcTime = start;
                    return data;
                }
            }
        }

        //真正的数据查询
        final Object[][] innerData = getFromDB(servicePo, sql, dbPo, currentSecond, parameter);

        long latency = System.currentTimeMillis() - start;
        LogUtil.info("latency:" + latency);


        D2Data data = new D2Data(new ArrayList<SerColumn>(servicePo.getColumns().size()));
        for (SerColumn columnPo : servicePo.getColumns()) {
            data.getColumnList().add(columnPo);
        }

        data.setData(innerData);

        if (cacheTime > 0 && cache != null && innerData.length > 0) {

            if (cacheTime >= Services.RELOAD_CACHETIME_THRESHOLD && latency <= Services.RELOAD_LATENCY_THRESHOLD) {
                data.setProcessInfo(currentSecond, latency, parameter, serviceName, timestamp, start);
            }
            cache.put(sql, data);

        }

        if (LogUtil.isDebugEnabled()) {
            LogUtil.debug("rows " + data.getData().length + ", columns: " + data.getColumnList().size());
        }
        return data;
    }

    /**
     * 依据sql, 从指定的数据库中进行查询, 查询完后将对应的连接还回到池中
     * @param info
     * @param sql
     * @param dbPo
     * @param currentSecond
     * @param parameter
     * @return
     * @throws com.rule.data.exception.RengineException
     */
    private static Object[][] getFromDB(SerService info, String sql, SerDb dbPo, long currentSecond, Map<String, Object> parameter)
            throws RengineException {
        final String dbID = dbPo.getDbID();
        ArrayBlockingQueue<RengineConnection> connections = CONN_POOL.get(dbID);

        if (connections == null) {
            connections = new ArrayBlockingQueue<RengineConnection>(100);
            ArrayBlockingQueue<RengineConnection> connectionsOld = CONN_POOL.putIfAbsent(dbID, connections);
            if (connectionsOld != null) {
                connections = connectionsOld;
            }
        }

        final String name = info.getName();
        RengineConnection conn = connections.poll();

        try {
            if (conn == null || !isValid(conn, currentSecond)) {
                Properties p = new Properties();
                p.setProperty("user", dbPo.getUser());
                p.setProperty("password", dbPo.getPassword());
                p.setProperty("connectTimeout", "100");

                Connection jdbcConn = ((Driver) Class.forName(dbPo.getDriver()).newInstance()).
                        connect(dbPo.getUrl(), p);
                if (conn == null) {
                    conn = new RengineConnection(jdbcConn, currentSecond);
                } else {
                    conn.jdbcConn = jdbcConn;
                }
            }
        } catch (Exception e) {
            throw new RengineException(name, "数据库" + dbID + "连接失败, " + ", " + e.getMessage());
        }

        try {
            if (LogUtil.isDebugEnabled()) {
                LogUtil.debug("sql: " + sql);
            }
            Statement st = null;
            ResultSet rs = null;
            ResultSetMetaData metaData;

            st = conn.jdbcConn.createStatement();
            rs = st.executeQuery(sql);
            metaData = rs.getMetaData();

            try {
                List<Object[]> dataFromDB = new ArrayList<Object[]>();

                int maxColumnIndex = -1;
                final List<SerColumn> columnPos = info.getColumns();

                for (SerColumn column : columnPos) {
                    final int index = column.getColumnIntIndex();
                    maxColumnIndex = index > maxColumnIndex ? index : maxColumnIndex;
                }

                int[] types = new int[maxColumnIndex + 1];
                int[] indexInResultSets = new int[maxColumnIndex + 1];
                boolean needInit = true;

                while (rs.next()) {
                    if (needInit) {
                        needInit = false;
                        for (SerColumn column : columnPos) {
                            final String sqlColumnName = column.getSqlColumnName();
                            final int index = column.getColumnIntIndex();
                            final int indexInResultSet = rs.findColumn(sqlColumnName);
                            final int type = metaData.getColumnType(indexInResultSet);

                            indexInResultSets[index] = indexInResultSet;
                            types[index] = type;
                        }
                    }

                    Object[] rowData = new Object[maxColumnIndex + 1];

                    for (SerColumn column : columnPos) {
                        final int index = column.getColumnIntIndex();
                        final int indexInResultSet = indexInResultSets[index];
                        final int type = types[index];

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
                                rowData[index] = rs.getString(indexInResultSet);
                                break;
                            case Types.BIGINT:
                            case Types.INTEGER:
                            case Types.BIT:
                            case Types.TINYINT:
                            case Types.SMALLINT:
                                try {
                                    rowData[index] = rs.getLong(indexInResultSet);
                                } catch (Exception e) {
                                    rowData[index] = 0L;
                                }

                                if (rs.wasNull()) {
                                    rowData[index] = null;
                                }
                                break;
                            case Types.DECIMAL:
                            case Types.DOUBLE:
                            case Types.FLOAT:
                            case Types.NUMERIC:
                            case Types.REAL:
                                rowData[index] = rs.getDouble(indexInResultSet);
                                if (rs.wasNull()) {
                                    rowData[index] = null;
                                }
                                break;
                            case Types.DATE:
                                try {
                                    java.util.Date date = rs.getDate(indexInResultSet);
                                    if (rs.wasNull()) {
                                        rowData[index] = null;
                                    } else {
                                        rowData[index] = new java.util.Date(date.getTime());
                                    }
                                } catch (Throwable t) {
                                    rowData[index] = null;
                                }
                                break;
                            case Types.TIME:
                                try {
                                    java.util.Date date = rs.getTime(indexInResultSet);
                                    if (rs.wasNull()) {
                                        rowData[index] = null;
                                    } else {
                                        rowData[index] = new java.util.Date(date.getTime());
                                    }
                                } catch (Throwable t) {
                                    rowData[index] = null;
                                }
                                break;
                            case Types.TIMESTAMP:
                                try {
                                    java.util.Date date = rs.getTimestamp(indexInResultSet);
                                    if (rs.wasNull()) {
                                        rowData[index] = null;
                                    } else {
                                        rowData[index] = new java.util.Date(date.getTime());
                                    }
                                } catch (Throwable t) {
                                    rowData[index] = null;
                                }
                                break;
                            default:
                                LogUtil.error("type not support " + type);
                                break;
                        }
                    }

                    dataFromDB.add(rowData);

                    if (dataFromDB.size() > db_row_max) {
                        throw new Exception("数据源行数超过阀值(" + db_row_max + "), " + dataFromDB.size());
                    }
                }

                final int size = dataFromDB.size();

                Object[][] data = new Object[size][];

                for (int i = 0; i < size; i++) {
                    data[i] = dataFromDB.get(i);
                }

                return data;
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            }
        } catch (SQLSyntaxErrorException e) {
            String str = parameter == null ? "{}" : JSON.toJSONString(parameter);
            String error = e.getMessage();
            throw new RengineException(name, "SQL语法错误(" + error + "), 参数" + str);
        } catch (Exception e) {
            String str = parameter == null ? "{}" : JSON.toJSONString(parameter);
            if (e.getMessage().startsWith("Unexpected exception encountered during query")) {     // llcheng-start
                try {
                    LogUtil.error("查询异常的sql:" + sql);

                    DatabaseMetaData meta = conn.jdbcConn.getMetaData();
                    Properties p = new Properties();
                    p.setProperty("user", dbPo.getUser());
                    p.setProperty("password", dbPo.getPassword());
                    p.setProperty("connectTimeout", "100");
                    Connection jdbcConn = ((Driver) Class.forName(dbPo.getDriver()).newInstance()).
                            connect(dbPo.getUrl(), p);
                    Statement st = null;
                    ResultSet rs = null;
                    st = jdbcConn.createStatement();
                    rs = st.executeQuery(sql);
                    ResultSetMetaData metaData = null;
                    metaData = rs.getMetaData();

                    while (rs.next()) {
                        LogUtil.info("查询出的第一个字段内容：" + rs.getObject(1));
                    }
                    System.out.println("驱动版本:" + meta.getDriverVersion());
                    int count = metaData.getColumnCount();
                    for (int i = 1; i <= count; i++) {
                        LogUtil.info("字段名称:" + metaData.getColumnName(i));
                        LogUtil.info("字段类型:" + metaData.getColumnTypeName(i));
                        LogUtil.info("字段长度:" + metaData.getColumnDisplaySize(i));
                        LogUtil.info("数据表名:" + metaData.getTableName(i));
                        LogUtil.info("----------------------------------------");
                    }
                } catch (Exception ex) {
                    LogUtil.error("", ex);
                    if (ex.getMessage().startsWith("Unexpected exception encountered during query")) {
                        try {
                            LogUtil.error("第二层查询异常的sql:" + sql);
                            DatabaseMetaData meta = conn.jdbcConn.getMetaData();
                            Properties p = new Properties();
                            p.setProperty("user", dbPo.getUser());
                            p.setProperty("password", dbPo.getPassword());
                            p.setProperty("connectTimeout", "100");
                            Connection jdbcConn = ((Driver) Class.forName(dbPo.getDriver()).newInstance()).
                                    connect(dbPo.getUrl(), p);
                            Statement st = null;
                            ResultSet rs = null;
                            st = jdbcConn.createStatement();
                            int index = sql.indexOf(" FROM ", 0);
                            boolean flag = true;
                            if (index == -1) {
                                index = sql.indexOf(" from ", 0);
                                flag = false;
                            }


                            String newSql = "";
                            if (index == -1) {
                                newSql = sql;
                                LogUtil.info("sql语句没有from：" + sql);
                            } else {
                                String[] sqls = null;
                                if (flag) {
                                    sqls = sql.split(" FROM ");
                                } else {
                                    sqls = sql.split(" from ");
                                }
                                newSql = sqls[0];
                                newSql = newSql + " ,1 from ";
                                newSql = newSql + sqls[1];
                            }
                            LogUtil.error("容错sql:" + newSql);
                            rs = st.executeQuery(newSql);
                            ResultSetMetaData metaData = null;
                            metaData = rs.getMetaData();

                            while (rs.next()) {
                                LogUtil.info("查询出的第一个字段内容：" + rs.getObject(1));
                            }

                        } catch (Exception exc) {
                            LogUtil.error("", exc);
                        }
                    }
                }
            }
            throw new RengineException(name, "数据库查询失败, " + e.getMessage() + ", 参数" + str);
        } finally {
            try {
                if (!connections.offer(conn)) {
                    if (!conn.jdbcConn.getAutoCommit()) {
                        conn.jdbcConn.rollback();
                    }

                    conn.jdbcConn.close();
                }
            } catch (Exception e) {
                LogUtil.error("error offer conn, " + e.getMessage());
            }
        }
    }

    private static boolean isValid(RengineConnection conn, long currentSecond) throws SQLException {
        if (conn.jdbcConn.isClosed()) {
            conn.lastCheckTime = currentSecond;
            return false;
        }

        long elapsed = currentSecond - conn.lastCheckTime;
        if (elapsed < 19) { // 19s开始检测
            return true;
        }

        conn.lastCheckTime = currentSecond;
        return conn.jdbcConn.isValid(5);
    }


    // 基础数据源的执行计划
    public static List<LinkedHashMap<String, Object>> explain(String serviceName, String sql, String dbID, Map<String, Object> param)
            throws RengineException, SQLException {
        if (sql.trim().startsWith("call") || sql.trim().startsWith("Call") || sql.trim().startsWith("CALL")) {
            return new ArrayList<LinkedHashMap<String, Object>>();
            //
        } else {
            final SerDb dbPo = Services.getDB(dbID);
            if (dbPo == null) {
                throw new RengineException(serviceName + "-执行计划", "所需数据库" + dbID + "的连接信息未找到");
            }
            if (param != null) {
                sql = "explain " + SqlUtil.renderSql("执行计划", sql, param);
            } else {
                sql = "explain " + sql;
            }
            ArrayBlockingQueue<RengineConnection> connections = CONN_POOL.get(dbID);
            if (connections == null) {
                connections = new ArrayBlockingQueue<RengineConnection>(100);
                ArrayBlockingQueue<RengineConnection> connectionsOld = CONN_POOL.putIfAbsent(dbID, connections);
                if (connectionsOld != null) {
                    connections = connectionsOld;
                }
            }
            RengineConnection conn = connections.poll();
            try {
                final long start = System.currentTimeMillis();
                final long currentSecond = start / 1000;
                if (conn == null || !isValid(conn, currentSecond)) {
                    Properties p = new Properties();
                    p.setProperty("user", dbPo.getUser());
                    p.setProperty("password", dbPo.getPassword());
                    p.setProperty("connectTimeout", "100");

                    Connection jdbcConn = ((Driver) Class.forName(dbPo.getDriver()).newInstance()).
                            connect(dbPo.getUrl(), p);
                    if (conn == null) {
                        conn = new RengineConnection(jdbcConn, currentSecond);
                    } else {
                        conn.jdbcConn = jdbcConn;
                    }
                }
            } catch (Exception e) {
                throw new RengineException(serviceName + "-执行计划", "数据库" + dbID + "连接失败, " + ", " + e.getMessage());
            }
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Statement st = null;
            ResultSet rs = null;
            ResultSetMetaData metaData = null;
            try {
                st = conn.jdbcConn.createStatement();
                rs = st.executeQuery(sql);
                metaData = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    for (int i = 0, size = metaData.getColumnCount(); i < size; ++i) {
                        String columName = metaData.getColumnLabel(i + 1);
                        Object value = rs.getObject(i + 1);
                        row.put(columName, value);
                    }
                    result.add(row);
                }
            } catch (SQLException se) {
                throw new RengineException(serviceName + "-执行计划", "数据库查询失败");
            } finally {
                if (!connections.offer(conn)) {
                    if (!conn.jdbcConn.getAutoCommit()) {
                        conn.jdbcConn.rollback();
                    }
                    conn.jdbcConn.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            }
            List<LinkedHashMap<String, Object>> explainInfos = new ArrayList<LinkedHashMap<String, Object>>();
            for (int i = 0; i < result.size(); i++) {
                LinkedHashMap<String, Object> explainMap = new LinkedHashMap<String, Object>();
                Map<String, Object> map = result.get(i);
                Object id = map.get("id");
                if (id == null) {
                    id = "";
                }
                explainMap.put("id", id);
                Object select_type = map.get("select_type");
                if (select_type == null) {
                    select_type = "";
                }
                explainMap.put("select_type", select_type);
                Object table = map.get("table");
                if (table == null) {
                    table = "";
                }
                explainMap.put("table", table);
                Object type = map.get("type");
                if (type == null) {
                    type = "";
                }
                explainMap.put("type", type);
                Object possible_keys = map.get("possible_keys");
                if (possible_keys == null) {
                    possible_keys = "";
                }
                explainMap.put("possible_keys", possible_keys);
                Object key = map.get("key");
                if (key == null) {
                    key = "";
                }
                explainMap.put("key", key);
                Object key_len = map.get("key_len");
                if (key_len == null) {
                    key_len = "";
                }
                explainMap.put("key_len", key_len);
                Object ref = map.get("ref");
                if (ref == null) {
                    ref = "";
                }
                explainMap.put("ref", ref);
                Object rows = map.get("rows");
                if (rows == null) {
                    rows = "";
                }
                explainMap.put("rows", rows);
                Object Extra = map.get("Extra");
                if (Extra == null) {
                    Extra = "";
                }
                explainMap.put("Extra", Extra);
                explainInfos.add(explainMap);
            }
            return explainInfos;
        }
    }
}
