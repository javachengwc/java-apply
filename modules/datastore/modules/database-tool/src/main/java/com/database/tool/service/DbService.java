package com.database.tool.service;

import com.database.tool.vo.Node;
import com.database.tool.vo.Result;

import java.util.List;

public interface DbService {

    List<?> findAllNodes();

    Result query(String sql);

    List<?> getColumns(String tableName);

    List<?> refresh(Node node);
}
