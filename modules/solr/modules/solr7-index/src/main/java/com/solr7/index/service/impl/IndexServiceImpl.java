package com.solr7.index.service.impl;

import com.solr7.index.annotation.SolrField;
import com.solr7.index.config.SolrConfig;
import com.solr7.index.model.IndexModel;
import com.solr7.index.service.IndexService;
import com.solr7.index.solr.SolrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Service
public class IndexServiceImpl implements IndexService {

    private static Logger logger= LoggerFactory.getLogger(IndexServiceImpl.class);

    @Autowired
    private SolrService solrService;

    @Autowired
    private SolrConfig solrConfig;

    public int addOrUptIndex(String id) {
        logger.info("IndexServiceImpl addOrUptIndex start,id={}",id);
        int result=-1;
        IndexModel indexModel=queryIndexInfo(id);
        if(indexModel==null) {
            logger.info("IndexServiceImpl addOrUptIndex queryIndexInfo result is null ,id={}",id);
            return result;
        }
        Map<String,Object> dataMap = transSolrData(indexModel);
        if(dataMap==null) {
            logger.info("IndexServiceImpl addOrUptIndex transSolrData result is null ,id={}",id);
            return result;
        }
        String demoCollect = solrConfig.getDemoCollectName();
        try{
            result = solrService.addOrUptDocument(demoCollect,dataMap);
        } catch (Exception e) {
            logger.info("IndexServiceImpl addOrUptIndex error,id={}",id);
        }
        logger.info("IndexServiceImpl addOrUptIndex end,id={},result={}",id,result);
        return result;
    }

    public int cancelIndex(String id) {
        logger.info("IndexServiceImpl cancelIndex start,id={}",id);
        String demoCollect = solrConfig.getDemoCollectName();
        int result=-1;
        try{
            result = solrService.deleteDocument(demoCollect,id);
        } catch (Exception e) {
            logger.info("IndexServiceImpl cancelIndex error,id={}",id);
        }
        logger.info("IndexServiceImpl cancelIndex end,id={},result={}",id,result);
        return result;
    }

    public Map<String,Object> transSolrData(IndexModel indexModel) {
        Map<String,Object> dataMap = new HashMap<String,Object>();
        try{
            Field[] fs = IndexModel.class.getDeclaredFields();
            for(Field f:fs) {
                SolrField solrField=f.getAnnotation(SolrField.class);
                if(solrField==null) {
                    continue;
                }
                String fieldName = solrField.value();
                f.setAccessible(true);
                Object value = f.get(indexModel);
                dataMap.put(fieldName,value);
            }
        }catch(Exception e) {
            logger.error("IndexServiceImpl transSolrData error,",e);
            return null;
        }
        return dataMap;
    }

    public IndexModel queryIndexInfo(String id) {
        IndexModel indexModel = new IndexModel();
        indexModel.setId(id);
        indexModel.setName("name");
        indexModel.setCity("city");
        indexModel.setImg("img");
        indexModel.setCreateTime("2017-01-02 00:00:01");
        return indexModel;
    }

}
