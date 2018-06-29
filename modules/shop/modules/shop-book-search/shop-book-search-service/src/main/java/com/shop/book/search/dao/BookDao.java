package com.shop.book.search.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface BookDao {

    public Map<String,Object> queryBookIndexInfo(@Param("bookId") Long bookId);
}
