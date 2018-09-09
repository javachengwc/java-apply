package com.mybatis.pseudocode.mybatisspring.support;

import com.mybatis.pseudocode.mybatis.session.SqlSession;
import com.mybatis.pseudocode.mybatis.session.SqlSessionFactory;
import com.mybatis.pseudocode.mybatisspring.SqlSessionTemplate;
import org.springframework.dao.support.DaoSupport;

public abstract class SqlSessionDaoSupport extends DaoSupport
{
    private SqlSession sqlSession;
    private boolean externalSqlSession;

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory)
    {
        if (!this.externalSqlSession)
            this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
    }

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate)
    {
        this.sqlSession = sqlSessionTemplate;
        this.externalSqlSession = true;
    }

    public SqlSession getSqlSession()
    {
        return this.sqlSession;
    }

    protected void checkDaoConfig()
    {
        //Assert.notNull(this.sqlSession, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
    }
}
