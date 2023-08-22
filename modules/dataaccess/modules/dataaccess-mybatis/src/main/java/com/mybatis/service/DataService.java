package com.mybatis.service;

import com.mybatis.model.entity.Data;

import java.util.List;

/**
 * 数据服务
 */
public interface DataService {

    List<Data> queryList();

}
