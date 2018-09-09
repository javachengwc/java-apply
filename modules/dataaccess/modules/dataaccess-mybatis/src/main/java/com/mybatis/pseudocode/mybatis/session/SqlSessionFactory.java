package com.mybatis.pseudocode.mybatis.session;


import java.sql.Connection;

public abstract interface SqlSessionFactory
{
    public abstract SqlSession openSession();

    public abstract SqlSession openSession(boolean paramBoolean);

    public abstract SqlSession openSession(Connection connection);

    //事务隔离级别
    public abstract SqlSession openSession(TransactionIsolationLevel transactionIsolationLevel);

    public abstract SqlSession openSession(ExecutorType executorType);

    public abstract SqlSession openSession(ExecutorType executorType, boolean paramBoolean);

    public abstract SqlSession openSession(ExecutorType executorType, TransactionIsolationLevel transactionIsolationLevel);

    public abstract SqlSession openSession(ExecutorType executorType, Connection connection);

    public abstract Configuration getConfiguration();
}
