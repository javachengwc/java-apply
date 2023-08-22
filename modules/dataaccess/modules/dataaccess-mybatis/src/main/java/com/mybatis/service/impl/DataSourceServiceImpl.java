package com.mybatis.service.impl;

import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidDataSource;
import com.mybatis.service.DataSourceService;
import com.mybatis.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DataSourceServiceImpl implements DataSourceService {

    private static Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    @Override
    public Object queryDataSourceInfo() {
        logger.info("DataSourceServiceImpl queryDataSourceInfo start...");
        DruidDataSource ds = (DruidDataSource) SpringContextUtil.getBean(DruidDataSource.class);
        logger.info(ds.toString());
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("stat",ds.getDataSourceStat());
        map.put("poolingCount",ds.getPoolingCount());
        map.put("activeCount",ds.getActiveCount());
        map.put("connectCount",ds.getConnectCount());
        map.put("closeCount",ds.getCloseCount());

        return map;
    }

}
