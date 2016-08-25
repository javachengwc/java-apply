package com.mountain.manage.dao;

import com.mountain.manage.util.SpringContextUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 基础dao类
 */
public abstract class BaseDao extends SqlSessionDaoSupport {

    private static Logger logger = LoggerFactory.getLogger(BaseDao.class);

    protected int batchCount = 500;

    protected int insertBatch(String sqlId, List<?> list) {

        int total= (list==null)?0:list.size();
        int doCnt=0;
        int times = total/batchCount;
        if((total%batchCount)>0)
        {
            times+=1;
        }

        for(int i=0;i<times;i++)
        {
            int start = i*batchCount;
            int end =(i+1)*batchCount;
            if((i+1)>=times)
            {
                end=total;
            }
            List<?> subList = list.subList(start, end);

            int insertCnt = this.getSqlSession().insert(sqlId, subList);
            logger.info(sqlId + "BaseDao插入数据条数:" + insertCnt + ",从第" + (start+1) + "条到第" + end+"条");
            doCnt += insertCnt;
        }
        logger.info("BaseDao完成批量插入" + sqlId);
        return doCnt;
    }

    @PostConstruct
    public void injectTemplate()
    {
        SqlSessionTemplate template=SpringContextUtils.getBean("sqlSessionTemplate",SqlSessionTemplate.class);
        this.setSqlSessionTemplate(template);
    }
}