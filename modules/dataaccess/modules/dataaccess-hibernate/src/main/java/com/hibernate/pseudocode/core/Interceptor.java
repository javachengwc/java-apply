package com.hibernate.pseudocode.core;


import com.hibernate.pseudocode.core.type.Type;

import java.io.Serializable;
import java.util.Iterator;

public abstract interface Interceptor
{
    public abstract boolean onLoad(Object object, Serializable serializable, Object[] arrayObject, String[] arrayStr, Type[] arrayType) throws CallbackException;

    public abstract boolean onFlushDirty(Object object, Serializable serializable, Object[] arrayObject1, Object[] arrayObject2, String[] arrayStr, Type[] arrayType)  throws CallbackException;

    public abstract boolean onSave(Object object, Serializable serializable, Object[] arrayObject, String[] arrayStr, Type[] arrayType) throws CallbackException;

    public abstract void onDelete(Object object, Serializable serializable, Object[] arrayObject, String[] arrayStr, Type[] arrayType) throws CallbackException;

    public abstract void onCollectionRecreate(Object object, Serializable serializable) throws CallbackException;

    public abstract void onCollectionRemove(Object object, Serializable serializable) throws CallbackException;

    public abstract void onCollectionUpdate(Object object, Serializable serializable) throws CallbackException;

    public abstract void preFlush(Iterator iterator) throws CallbackException;

    public abstract void postFlush(Iterator iterator) throws CallbackException;

    public abstract Boolean isTransient(Object object);

    public abstract int[] findDirty(Object object, Serializable serializable, Object[] array1, Object[] array2, String[] arrayStr, Type[] arrayType);

    public abstract Object instantiate(String paramString, EntityMode entityMode, Serializable serializable) throws CallbackException;

    public abstract String getEntityName(Object object)  throws CallbackException;

    public abstract Object getEntity(String param, Serializable serializable) throws CallbackException;

    public abstract void afterTransactionBegin(Transaction transaction);

    public abstract void beforeTransactionCompletion(Transaction transaction);

    public abstract void afterTransactionCompletion(Transaction transaction);

    public abstract String onPrepareStatement(String param);
}
