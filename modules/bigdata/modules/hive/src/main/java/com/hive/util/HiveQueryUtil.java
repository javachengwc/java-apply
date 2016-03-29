package com.hive.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * hive查询工具类
 */
public class HiveQueryUtil {

    private static Logger logger = LoggerFactory.getLogger(HiveQueryUtil.class);

    public static List<Map<String, Object>> queryList(JdbcTemplate jdbcTemplate, String sql){
        List<Map<String, Object>> result = null;
        logger.info("开始queryList："+sql);
        try {
            result = jdbcTemplate.queryForList(sql);
        } catch (DataAccessException e) {
            logger.error("查询数据出现异常"+sql, e);
        }
        if(null == result){
            result = new ArrayList<Map<String, Object>>(0);
        }
        logger.info("queryList结束："+sql+" 查询结果数：" + result.size());
        return result;
    }

    public static Map<String, Object> queryUnique(JdbcTemplate jdbcTemplate, String sql){
        Map<String, Object> result = null;
        logger.info("开始queryUnique："+sql);
        try {
            result = jdbcTemplate.queryForMap(sql);
        } catch (DataAccessException e) {
            logger.error("查询数据出现异常："+sql, e);
        }
        if(null == result){
            result = new HashMap<String, Object>(0);
        }
        int size = result.isEmpty() ? 0 : 1;
        logger.info("queryUnique结束："+sql+" 查询结果数：" + size);
        return result;
    }

    public static List<Map<String, Object>> queryList(JdbcTemplate jdbcTemplate, String sqlFormat, Object... args){
        String sql = String.format(sqlFormat, args);
        return queryList(jdbcTemplate, sql);
    }

    public static Map<String, Object> queryUnique(JdbcTemplate jdbcTemplate, String sqlFormat, Object... args){
        String sql = String.format(sqlFormat, args);
        return queryUnique(jdbcTemplate, sql);
    }
}
