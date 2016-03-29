package com.configcenter.service.core;

import com.configcenter.dao.IDao;
import com.configcenter.vo.CommonQueryVo;

import java.util.List;

/**
 * 基础服务类
 */
public abstract  class BaseService<T> implements IService<T>  {

    public abstract IDao<T> getDao();

    public int countAll()
    {
        return getDao().countAll();
    }

    public T getById(T t)
    {
        return getDao().getById(t);
    }

    public int add(T t)
    {
        return getDao().add(t);
    }

    public void batchAdd(List<T> list)
    {
        getDao().batchAdd(list);
    }

    public int update(T t)
    {
        return getDao().update(t);
    }

    public void delete(T t)
    {
        getDao().delete(t);
    }

    public List<T> queryList(CommonQueryVo queryVo)
    {
       return  getDao().queryList(queryVo);
    }

    public int count(CommonQueryVo queryVo)
    {
        return getDao().count(queryVo);
    }
}
