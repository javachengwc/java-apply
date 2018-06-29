package com.shop.book.search.service.impl;

import com.shop.book.search.config.SolrConfig;
import com.shop.book.search.dao.BookDao;
import com.shop.book.search.service.BookIndexService;
import com.shop.book.search.solr.SolrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookIndexServiceImpl implements BookIndexService {

    private static Logger logger = LoggerFactory.getLogger(BookIndexServiceImpl.class);

    @Autowired
    private SolrService solrService;

    @Autowired
    private SolrConfig solrConfig;

    @Autowired
    private BookDao bookDao;

    public int addOrUptIndex(Long bookId) {
        logger.info("BookIndexServiceImpl addOrUptIndex start,bookId={}", bookId);
        String bookCollect = solrConfig.getBookCollect();
        int result = -1;
        Map<String,Object> bookInfo = bookDao.queryBookIndexInfo(bookId);
        if (bookInfo == null) {
            logger.info("BookIndexServiceImpl addOrUptIndex queryBookIndexInfo result is null ,bookId={}", bookId);
            return result;
        }
        try {
            result = solrService.addOrUptDocument(bookCollect, bookInfo);
        } catch (Exception e) {
            logger.error("BookIndexServiceImpl addOrUptIndex error,bookId={}", bookId,e);
        }
        logger.info("BookIndexServiceImpl addOrUptIndex end,bookId={},result={}", bookId, result);
        return result;
    }

    public int cancelIndex(Long bookId) {
        logger.info("BookIndexServiceImpl cancelIndex start,bookId={}", bookId);
        String bookCollect = solrConfig.getBookCollect();
        int result = -1;
        try {
            result = solrService.deleteDocument(bookCollect,""+bookId);
        } catch (Exception e) {
            logger.info("BookIndexServiceImpl cancelIndex error,bookId={}", bookId);
        }
        logger.info("BookIndexServiceImpl cancelIndex end,bookId={},result={}", bookId, result);
        return result;
    }
}
