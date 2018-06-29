package com.shop.book.search.service;

import com.shop.base.model.Page;
import com.shop.book.search.api.model.BookQueryVo;
import com.shop.book.search.model.BookSimple;

public interface BookSearchService {

    public Page<BookSimple> queryPage(BookQueryVo queryVo);

    public int count(BookQueryVo queryVo);
}
