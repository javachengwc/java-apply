package com.shop.book.search.service;

public interface BookIndexService {

    public int addOrUptIndex(Long bookId);

    public int cancelIndex(Long bookId);
}
