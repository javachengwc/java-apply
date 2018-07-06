package com.shop.book.service.manager;

import com.shop.book.model.pojo.Advert;
import com.shop.book.service.AdvertService;
import com.shop.book.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class AdvertManager {

    private static Logger logger= LoggerFactory.getLogger(AdvertManager.class);

    private static AdvertManager inst = new AdvertManager();

    public static AdvertManager getInstance() {
        return inst;
    }

    //位置广告数据
    private Map<String,List<Advert>> positionAdvertMap = new HashMap<String,List<Advert>>();

    private AdvertManager() {
        init();
    }

    public void init() {
        logger.info("AdvertManager init start................");
        AdvertService advertService = SpringContextUtils.getBean(AdvertService.class);
        List<Advert> list = advertService.queryUpAdertList();
        Map<String,List<Advert>> dataMap = new HashMap<String,List<Advert>>();
        for(Advert advert:list) {
            String positionCode = advert.getPositionCode();
            List<Advert> posAdvertList = dataMap.get(positionCode);
            if(posAdvertList==null) {
                posAdvertList= new ArrayList<Advert>();
            }
            posAdvertList.add(advert);
        }
        for(Map.Entry<String,List<Advert>> entry:dataMap.entrySet()) {
            String code= entry.getKey();
            List<Advert> perList = entry.getValue();
            int perCnt = perList==null?0:perList.size();
            logger.info("AdvertManager init  data ,positionCode={},posAdvertCount={}",code,perCnt);
        }
        positionAdvertMap=dataMap;
        logger.info("AdvertManager init end................");
    }

    public List<Advert> getAdvertByPosition(String positionCode) {
        List<Advert> list = positionAdvertMap.get(positionCode);
        if(list==null) {
            return Collections.EMPTY_LIST;
        }
        long nowMils= System.currentTimeMillis();
        List<Advert> fList = new ArrayList<Advert>();
        for(Advert advert:list) {
            Date startTime = advert.getStartTime();
            Date endTime = advert.getEndTime();
            if(startTime!=null && startTime.getTime()>nowMils) {
                continue;
            }
            if(endTime!=null && endTime.getTime()<nowMils) {
                continue;
            }
            fList.add(advert);
        }
        List<Advert> rtList =Collections.unmodifiableList(fList);
        return rtList;
    }
}
