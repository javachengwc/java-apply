package com.shop.book.search.service.impl;

import com.shop.base.model.Page;
import com.shop.book.search.api.model.BookQueryVo;
import com.shop.book.search.config.SolrConfig;
import com.shop.book.search.enums.BookSolrFieldEnum;
import com.shop.book.search.model.BookSimple;
import com.shop.book.search.service.BookSearchService;
import com.shop.book.search.solr.SolrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookSearchServiceImpl implements BookSearchService  {

    private static Logger logger= LoggerFactory.getLogger(BookSearchServiceImpl.class);

    private static Long zeroLong =0L;

    @Autowired
    private SolrService solrService;

    @Autowired
    private SolrConfig solrConfig;

    public Page<BookSimple> queryPage(BookQueryVo queryVo) {

        long pageNum =queryVo.getPageNum().longValue();
        long pageSize = queryVo.getPageSize().longValue();
        long start = queryVo.getStart().longValue();
        logger.debug("BookSearchServiceImpl queryPage start,pageNum={},pageSize={}",pageNum,pageSize);
        String bookCollect = solrConfig.getBookCollect();
        Map<String,Object> queryMap =tipQueryMap(queryVo);
        Map<String,String> sortMap = tipSortMap(queryVo);

        Page<BookSimple> rtPage = new Page<BookSimple>();
        rtPage.setTotalCount(zeroLong.intValue());
        try {
            Page<BookSimple> page=solrService.querySolrBeanPage(bookCollect,queryMap,sortMap,start,pageSize,BookSimple.class);
            rtPage.setTotalCount(page.getTotalCount());
            List<BookSimple> list = page.getList();
            if(list==null) {
                list= Collections.EMPTY_LIST;
            }
            rtPage.setList(list);
        }catch(Exception e) {
            logger.error("BookSearchServiceImpl queryPage error,pageNum={},pageSize={}",pageNum,pageSize,e);
        }
        return rtPage;
    }

    public int count(BookQueryVo queryVo) {
        String bookCollect = solrConfig.getBookCollect();
        Map<String,Object> queryMap =tipQueryMap(queryVo);
        int totalCount=0;
        try {
            Page<Object> pageResult = solrService.querySolrPage(bookCollect, queryMap, null, zeroLong, zeroLong);
            totalCount = pageResult.getTotalCount()==null?0:pageResult.getTotalCount().intValue();
        }catch(Exception e) {
            logger.error("BookSearchServiceImpl count error,",e);
        }
        return totalCount;
    }

    public Map<String,Object> tipQueryMap(BookQueryVo queryVo) {
        Map<String,Object> map = new HashMap<String,Object>();
        //关键字
        map.put(BookSolrFieldEnum.KEYWORD.getField(),queryVo.getKeyword());
        //状态
        map.put(BookSolrFieldEnum.STATU.getField(),queryVo.getStatu());
        //价格
        Integer minPrice =queryVo.getMinPrice();
        Integer maxPrice = queryVo.getMaxPrice();
        merginRangeCdn(map,minPrice,maxPrice,BookSolrFieldEnum.PRICE.getField());
        return map;
    }

    public void merginRangeCdn(Map<String,Object> queryMap,Number min,Number max,String solrField) {
        if(min==null && max==null) {
            return;
        }
        if(min!=null) {
            queryMap.put(solrField,"["+ min +" TO *]");
        }
        if(max!=null) {
            queryMap.put(solrField,"[* TO "+max+"]");
        }
        if(min!=null && max!=null) {
            queryMap.put(solrField,"["+ min +" TO "+max+"]");
        }
    }

    public Map<String,String > tipSortMap(BookQueryVo queryVo) {
        Map<String,String > sortMap = new HashMap<String,String>(1);
        sortMap.put(queryVo.getOrderByColumn(),queryVo.getOrderBySort());
        return sortMap;
    }
}
