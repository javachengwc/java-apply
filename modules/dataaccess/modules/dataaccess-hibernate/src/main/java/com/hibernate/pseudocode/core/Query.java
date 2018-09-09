package com.hibernate.pseudocode.core;


import com.hibernate.pseudocode.core.transform.ResultTransformer;
import com.hibernate.pseudocode.core.type.Type;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public abstract interface Query
{
    public abstract String getQueryString();

    public abstract Type[] getReturnTypes() throws HibernateException;

    public abstract String[] getReturnAliases() throws HibernateException;

    public abstract String[] getNamedParameters() throws HibernateException;

    public abstract Iterator iterate() throws HibernateException;

    //...

    public abstract List list() throws HibernateException;

    public abstract Object uniqueResult() throws HibernateException;

    public abstract int executeUpdate() throws HibernateException;

    public abstract Query setMaxResults(int paramInt);

    public abstract Query setFirstResult(int paramInt);

    public abstract boolean isReadOnly();

    public abstract Query setReadOnly(boolean paramBoolean);

    public abstract Query setCacheable(boolean paramBoolean);

    public abstract Query setCacheRegion(String paramString);

    public abstract Query setTimeout(int paramInt);

    public abstract Query setFetchSize(int paramInt);


    //...

    public abstract Query setParameter(int paramInt, Object paramObject, Type paramType);

    public abstract Query setParameter(String paramString, Object paramObject, Type paramType);

    public abstract Query setParameter(int paramInt, Object paramObject) throws HibernateException;

    public abstract Query setParameter(String paramString, Object paramObject) throws HibernateException;

    public abstract Query setParameters(Object[] paramArrayOfObject, Type[] paramArrayOfType) throws HibernateException;

    public abstract Query setParameterList(String paramString, Collection paramCollection, Type paramType) throws HibernateException;

    public abstract Query setParameterList(String paramString, Collection paramCollection) throws HibernateException;

    public abstract Query setParameterList(String paramString, Object[] paramArrayOfObject, Type paramType) throws HibernateException;

    public abstract Query setParameterList(String paramString, Object[] paramArrayOfObject) throws HibernateException;

    public abstract Query setProperties(Object paramObject) throws HibernateException;

    public abstract Query setProperties(Map paramMap) throws HibernateException;

    public abstract Query setString(int paramInt, String paramString);

    public abstract Query setCharacter(int paramInt, char paramChar);

    public abstract Query setBoolean(int paramInt, boolean paramBoolean);

    public abstract Query setByte(int paramInt, byte paramByte);

    public abstract Query setShort(int paramInt, short paramShort);

    public abstract Query setInteger(int paramInt1, int paramInt2);

    public abstract Query setLong(int paramInt, long paramLong);

    public abstract Query setFloat(int paramInt, float paramFloat);

    public abstract Query setDouble(int paramInt, double paramDouble);

    public abstract Query setBinary(int paramInt, byte[] paramArrayOfByte);

    public abstract Query setText(int paramInt, String paramString);

    public abstract Query setSerializable(int paramInt, Serializable paramSerializable);

    public abstract Query setLocale(int paramInt, Locale paramLocale);

    public abstract Query setBigDecimal(int paramInt, BigDecimal paramBigDecimal);

    public abstract Query setBigInteger(int paramInt, BigInteger paramBigInteger);

    public abstract Query setDate(int paramInt, Date paramDate);

    public abstract Query setTime(int paramInt, Date paramDate);

    public abstract Query setTimestamp(int paramInt, Date paramDate);

    public abstract Query setCalendar(int paramInt, Calendar paramCalendar);

    public abstract Query setCalendarDate(int paramInt, Calendar paramCalendar);

    public abstract Query setString(String paramString1, String paramString2);

    public abstract Query setCharacter(String paramString, char paramChar);

    public abstract Query setBoolean(String paramString, boolean paramBoolean);

    public abstract Query setByte(String paramString, byte paramByte);

    public abstract Query setShort(String paramString, short paramShort);

    public abstract Query setInteger(String paramString, int paramInt);

    public abstract Query setLong(String paramString, long paramLong);

    public abstract Query setFloat(String paramString, float paramFloat);

    public abstract Query setDouble(String paramString, double paramDouble);

    public abstract Query setBinary(String paramString, byte[] paramArrayOfByte);

    public abstract Query setText(String paramString1, String paramString2);

    public abstract Query setSerializable(String paramString, Serializable paramSerializable);

    public abstract Query setLocale(String paramString, Locale paramLocale);

    public abstract Query setBigDecimal(String paramString, BigDecimal paramBigDecimal);

    public abstract Query setBigInteger(String paramString, BigInteger paramBigInteger);

    public abstract Query setDate(String paramString, Date paramDate);

    public abstract Query setTime(String paramString, Date paramDate);

    public abstract Query setTimestamp(String paramString, Date paramDate);

    public abstract Query setCalendar(String paramString, Calendar paramCalendar);

    public abstract Query setCalendarDate(String paramString, Calendar paramCalendar);

    public abstract Query setEntity(int paramInt, Object paramObject);

    public abstract Query setEntity(String paramString, Object paramObject);

    public abstract Query setResultTransformer(ResultTransformer paramResultTransformer);
}
