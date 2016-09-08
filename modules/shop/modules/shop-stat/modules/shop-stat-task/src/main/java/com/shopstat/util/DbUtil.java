package com.shopstat.util;

import com.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * 数据访问工具类
 */
public class DbUtil {

    private static Logger logger = LoggerFactory.getLogger(DbUtil.class);

    public static <T> List<T> queryList(String sql ,Class<T> clazz,JdbcTemplate jdbcTemplate)
    {
        if(logger.isInfoEnabled())
        {
            logger.info("DbUtil queryList ,sql="+sql+"\r\nentity class="+clazz.getName());
        }
        List<Map<String,Object>> listMap = jdbcTemplate.queryForList(sql);
        if(listMap!=null) {
            List<T> list = MapUtil.listMap2ListBean(listMap, clazz,true);
            listMap.clear();
            return list;
        }
        return null;
    }

    public static <T> T queryBean(String sql ,Class<T> clazz,JdbcTemplate jdbcTemplate)
    {
        if(logger.isInfoEnabled())
        {
            logger.info("DbUtil queryBean ,sql="+sql+"\r\nentity class="+clazz.getName());
        }
        Map<String,Object> map = jdbcTemplate.queryForMap(sql);
        if(map!=null) {
            T bean = MapUtil.map2Bean(map,clazz,true);
            return bean;
        }
        return null;
    }

    public static List<Map<String,Object>> queryList(String sql ,JdbcTemplate jdbcTemplate)
    {
        if(logger.isInfoEnabled())
        {
            logger.info("DbUtil queryList ,sql="+sql);
        }
        List<Map<String,Object>> listMap = jdbcTemplate.queryForList(sql);
        return listMap;
    }

    public static Map<String,Object> queryMap(String sql ,JdbcTemplate jdbcTemplate)
    {
        if(logger.isInfoEnabled())
        {
            logger.info("DbUtil queryMap ,sql="+sql);
        }
        return jdbcTemplate.queryForMap(sql);
    }

    public static int count(String sql,JdbcTemplate jdbcTemplate)
    {
        if(logger.isInfoEnabled())
        {
            logger.info("DbUtil count ,sql="+sql);
        }
        return jdbcTemplate.queryForInt(sql);
    }
}
