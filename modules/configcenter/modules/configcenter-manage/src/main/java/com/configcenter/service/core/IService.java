package com.configcenter.service.core;

import com.configcenter.vo.CommonQueryVo;

import java.util.List;

/**
 * 服务接口
 */
public interface IService<T> {

    public int countAll();

    public T getById(T t);

    public int add(T t);

    public void batchAdd(List<T> list);

    public int update(T t);

    public void delete(T t);

    public List<T> queryList(CommonQueryVo queryVo);

    public int count(CommonQueryVo queryVo);
}
