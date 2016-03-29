package com.hive.service.impl;

import com.hive.service.AccessStatService;
import com.hive.util.HiveQueryUtil;
import com.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AccessStatServiceImpl implements AccessStatService {


    private static Logger logger = LoggerFactory.getLogger(AccessStatServiceImpl.class);

    //查pv
    private static String pvSql="select partitiontime,url,count(1) as pv \n" +
            " from accesslog \n" +
            " where partitiontime='%s' \n" +
            " group by partitiontime,url ";
    //%s  ------20150606

    //查uv
    private static String uvSql="select partitiontime,url ,count(distinct user) as uv \n" +
            " from accesslog \n" +
            " where partitiontime='%s' \n" +
            " group by partitiontime,url ";
    //%s  ------20150606

    @Autowired
    @Qualifier("jdbcTemplateHive")
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void hander(Date date)
    {

        String partitiontime= DateUtil.formatDate(date, "yyyyMMdd");

        logger.info("AccessStatServiceImpl hander start,date="+partitiontime);

        List<Map<String,Object>> pvList = queryPv(date);

        List<Map<String,Object>> uvList = queryUv(date);

        logger.info("AccessStatServiceImpl hander end");

    }

    /**查pv**/
    public List<Map<String,Object>> queryPv(Date date)
    {
        String partitionTime =  DateUtil.formatDate(date, "yyyyMMdd");
        String query = String.format(pvSql,partitionTime);
        List<Map<String, Object>> result = HiveQueryUtil.queryList(jdbcTemplate,query);
        return result;
    }

    /**查uv**/
    public List<Map<String,Object>> queryUv(Date date)
    {
        String partitionTime =  DateUtil.formatDate(date, "yyyyMMdd");
        String query = String.format(uvSql,partitionTime);
        List<Map<String, Object>> result = HiveQueryUtil.queryList(jdbcTemplate,query);
        return result;
    }

}
