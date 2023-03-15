package com.druid.pseudocode.druid.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.management.ObjectName;


public class DruidDataSourceUtils
{
    private static final Log LOG = LogFactory.getLog(DruidDataSourceUtils.class);

    public static String getUrl(Object druidDataSource) {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getUrl();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getUrl", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return (String)obj;
        } catch (Exception e) {
            LOG.error("getUrl error", e);
        }return null;
    }

    public static long getID(Object druidDataSource)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getID();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getID", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return ((Long)obj).longValue();
        } catch (Exception e) {
            LOG.error("getID error", e);
        }return -1L;
    }

    public static String getName(Object druidDataSource)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getName();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getName", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return (String)obj;
        } catch (Exception e) {
            LOG.error("getUrl error", e);
        }return null;
    }

    public static ObjectName getObjectName(Object druidDataSource)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getObjectName();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getObjectName", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return (ObjectName)obj;
        } catch (Exception e) {
            LOG.error("getObjectName error", e);
        }return null;
    }

    public static Object getSqlStat(Object druidDataSource, int sqlId)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getSqlStat(sqlId);
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getSqlStat", new Class[] { Integer.TYPE });
            return method.invoke(druidDataSource, new Object[] { Integer.valueOf(sqlId) });
        } catch (Exception e) {
            LOG.error("getSqlStat error", e);
        }return null;
    }

    public static boolean isRemoveAbandoned(Object druidDataSource)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).isRemoveAbandoned();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("isRemoveAbandoned", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return ((Boolean)obj).booleanValue();
        } catch (Exception e) {
            LOG.error("isRemoveAbandoned error", e);
        }return false;
    }

    public static Map<String, Object> getStatDataForMBean(Object druidDataSource)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getStatDataForMBean();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getStatDataForMBean", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return (Map)obj;
        } catch (Exception e) {
            LOG.error("getStatDataForMBean error", e);
        }return null;
    }

    public static Map<String, Object> getStatData(Object druidDataSource)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getStatData();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getStatData", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return (Map)obj;
        } catch (Exception e) {
            LOG.error("getStatData error", e);
        }return null;
    }

    public static Map getSqlStatMap(Object druidDataSource)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getSqlStatMap();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getSqlStatMap", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return (Map)obj;
        } catch (Exception e) {
            LOG.error("getSqlStatMap error", e);
        }return null;
    }

    public static Map<String, Object> getWallStatMap(Object druidDataSource)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getWallStatMap();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getWallStatMap", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return (Map)obj;
        } catch (Exception e) {
            LOG.error("getWallStatMap error", e);
        }return null;
    }

    public static List<Map<String, Object>> getPoolingConnectionInfo(Object druidDataSource)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getPoolingConnectionInfo();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getPoolingConnectionInfo", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return (List)obj;
        } catch (Exception e) {
            LOG.error("getPoolingConnectionInfo error", e);
        }return null;
    }

    public static List<String> getActiveConnectionStackTrace(Object druidDataSource)
    {
        if (druidDataSource.getClass() == DruidDataSource.class) {
            return ((DruidDataSource)druidDataSource).getActiveConnectionStackTrace();
        }
        try
        {
            Method method = druidDataSource.getClass().getMethod("getActiveConnectionStackTrace", new Class[0]);
            Object obj = method.invoke(druidDataSource, new Object[0]);
            return (List)obj;
        } catch (Exception e) {
            LOG.error("getActiveConnectionStackTrace error", e);
        }return null;
    }
}
