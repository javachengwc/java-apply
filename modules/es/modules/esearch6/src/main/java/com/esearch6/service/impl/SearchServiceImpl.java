package com.esearch6.service.impl;

import com.esearch6.model.SearchVo;
import com.esearch6.service.SearchService;
import com.util.page.Page;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    private static Logger logger= LoggerFactory.getLogger(SearchServiceImpl.class);

    private static Long zeroLong =0L;

    public static String SORT_ASC="asc";

    public static String SORT_DESC="desc";

    public Page<Map<String,Object>> queryPage(SearchVo searchVo) {

        Long page =(searchVo==null || searchVo.getPage()<1)?1L:searchVo.getPage();
        Long pageSize = (searchVo==null || searchVo.getPageSize()<=0)?10L:searchVo.getPageSize();
        long start  = (page-1)*pageSize;
        logger.info("SearchServiceImpl queryPage start,page={},pageSize={}",page,pageSize);
        String collectName = "";
        //TODO....................
        BoolQueryBuilder queryBuilder= new BoolQueryBuilder();

        Map<String,String> queryMap =tipQueryMap(searchVo);
        Map<String,String> sortMap = tipSortMap(searchVo);

        Page<Map<String,Object>> rtPage = new Page<Map<String,Object>>();
        rtPage.setTotalCount(0);
        rtPage.setPageNo(page.intValue());
        rtPage.setPageSize(pageSize.intValue());
        try {
            Page<Object> queryRt=new Page<Object>();
            //TODO....................

            rtPage.setTotalCount(queryRt.getTotalCount());
            List<Object> list = queryRt.getResult();
            if(list!=null) {
                List<Map<String,Object>> rtList = new ArrayList<Map<String,Object>>();
                for(Object per:list) {
                    Map<String,Object> map = (Map<String,Object>)per;
                    rtList.add(map);
                }
                rtPage.setResult(rtList);
            }
        }catch(Exception e) {
            logger.error("SearchServiceImpl queryPage error,page={},pageSize={}",page,pageSize,e);
        }
        return rtPage;
    }

    public Map<String,String> tipQueryMap(SearchVo searchVo) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("city",searchVo.getCity());//城市
        map.put("keyword",searchVo.getKeyword());//关键字
        //价格
        Integer minPrice =searchVo.getMinPrice();
        Integer maxPrice = searchVo.getMaxPrice();
        merginRangeCdn(map,minPrice,maxPrice,"price");
        return map;
    }


    public void merginRangeCdn(Map<String,String> queryMap,Number min,Number max,String solrField) {
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

    public Map<String,String > tipSortMap(SearchVo searchVo) {
        String order = searchVo.getOrder();
        if(StringUtils.isBlank(order)) {
            return null;
        }
        Integer desc = searchVo.getDesc();
        String sortStr = (desc==null ||desc==0)?SORT_ASC:SORT_DESC;

        Map<String,String > sortMap = new LinkedHashMap<String,String>();
        sortMap.put(order,sortStr);
        return sortMap;
    }

    public int count(SearchVo searchVo) {
        String collectName = "";
        //TODO....................
        Map<String,String> queryMap =tipQueryMap(searchVo);
        int totalCount=0;
        try {
            Page<Object> pageResult =new Page<Object>();
            //TODO....................
            totalCount = pageResult.getTotalCount();
        }catch(Exception e) {
            logger.error("SearchServiceImpl count error,",e);
        }
        return totalCount;
    }
}
