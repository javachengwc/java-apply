package com.mybatis.pseudocode.mybatisspring;


import com.mybatis.pseudocode.mybatis.session.ExecutorType;
import com.mybatis.pseudocode.mybatis.session.SqlSession;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.transaction.support.ResourceHolderSupport;

public final class SqlSessionHolder extends ResourceHolderSupport
{
    private final SqlSession sqlSession;
    private final ExecutorType executorType;
    private final PersistenceExceptionTranslator exceptionTranslator;

    public SqlSessionHolder(SqlSession sqlSession, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator)
    {
        this.sqlSession = sqlSession;
        this.executorType = executorType;
        this.exceptionTranslator = exceptionTranslator;
    }

    public SqlSession getSqlSession() {
        return this.sqlSession;
    }

    public ExecutorType getExecutorType() {
        return this.executorType;
    }

    public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
        return this.exceptionTranslator;
    }
}
