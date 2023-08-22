package com.mybatis.service.impl;

import com.mybatis.model.entity.Cache;
import com.mybatis.model.entity.CacheTable;
import com.mybatis.service.MybatisDirectAccessDBService;
import com.mybatis.util.SpringContextUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

@Service
public class MybatisDirectAccessDBServiceImpl implements MybatisDirectAccessDBService {

    private static Logger logger = LoggerFactory.getLogger(MybatisDirectAccessDBServiceImpl.class);

    @Override
    public boolean initDataWithDirectAccess() {
        logger.info("MybatisDirectAccessDBServiceImpl initDataWithDirectAccess start...");
        DefaultSqlSessionFactory ssf = (DefaultSqlSessionFactory) SpringContextUtil.getBean("sqlSessionFactory");
        SqlSession ss = null;
        Connection connection = null;
        try {
            ss = ssf.openSession();
            connection = ss.getConnection();
            initData(connection);
            Thread.sleep(10*1000L);
        } catch (Exception e) {
            logger.error("MybatisDirectAccessDBServiceImpl initDataWithDirectAccess error,",e);
        } finally {
            DbUtils.closeQuietly(connection);
            if (ss != null) {
                ss.close();
            }
        }
        return true;
    }

    private List<Cache> initData(Connection connection) {
        logger.info("MybatisDirectAccessDBServiceImpl initData start...");
        String sql = "select cache_name,sql_text from t_cache where flag =1";
        QueryRunner queryRunner = new QueryRunner();
        BeanListHandler<Cache> resultSetHandler = new BeanListHandler<Cache> (Cache.class);
        List<Cache> cacheList= null;
        try {
            cacheList = queryRunner.query(connection, sql, resultSetHandler);
        } catch (Exception e) {
            logger.error("MybatisDirectAccessDBServiceImpl initData error,",e);
        }
        return  cacheList;
    }

    private List<CacheTable> initData2(Connection connection) {
        logger.info("MybatisDirectAccessDBServiceImpl initData2 start...");
        String sql = "select * from cachetable where flag =1";
        QueryRunner queryRunner = new QueryRunner();
        BeanListHandler<CacheTable> resultSetHandler = new BeanListHandler<CacheTable> (CacheTable.class);
        List<CacheTable> cacheList= null;
        try {
            cacheList = queryRunner.query(connection, sql, resultSetHandler);
        } catch (Exception e) {
            logger.error("MybatisDirectAccessDBServiceImpl initData2 error,",e);
        }
        return  cacheList;
    }
}
