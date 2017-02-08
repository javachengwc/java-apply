package com.shopstat.executor.aftersale;

import com.shopstat.dao.ext.aftersale.SafeguardStatDao;
import com.shopstat.executor.IExecutor;
import com.shopstat.model.pojo.StatSafeguard;
import com.shopstat.model.vo.stat.Dimen;
import com.shopstat.model.vo.stat.Norm;
import com.shopstat.model.vo.stat.Stat;
import com.shopstat.util.DateTimeUtil;
import com.shopstat.util.StatTotalUtil;
import com.util.enh.RandomUtil;
import com.util.date.DateUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 维权统计类
 */
@Service
public class SafeguardStatExecutor  implements IExecutor{

    private Logger logger = LoggerFactory.getLogger(SafeguardStatExecutor.class);

    @Autowired
    private SafeguardStatDao safeguardStatDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void exec(DateTime dateTime)
    {
        logger.info("SafeguardStatExecutor exec begin,dateTime ="+dateTime);

        Date date = DateTimeUtil.getDate(dateTime.toString(DateTimeUtil.FMT_YMD));
        //清数据
        safeguardStatDao.deleteByDate(date);
        //各维度列表
        List<String> fromSources =new LinkedList<String>();
        fromSources.add("0");//全部
        fromSources.add("1");
        fromSources.add("2");

        //在用StatTotalUtil在数据总和统计的时候，各维度列表不需要一一写出来
        List<String> starters= new LinkedList<String>();
//        starters.add("0");
//        starters.add("1");
//        starters.add("2");

        List<String> tagIds= new LinkedList<String>();
//        tagIds.add("0");//全部
//        tagIds.add("1");
//        tagIds.add("2");
//        tagIds.add("3");

        List<String> subIds= new LinkedList<String>();
//        subIds.add("0");//全部
//        for(String tagId:tagIds)
//        {
//            int tid =Integer.parseInt(tagId);
//            if(tid>0)
//            {
//
//                subIds.add(""+(tid*10));
//                subIds.add(""+(tid*10+1));
//                subIds.add(""+(tid*10+2));
//            }
//        }

        List<String> thirdIds= new LinkedList<String>();
//        thirdIds.add("0");//全部
//        for(String subId:subIds)
//        {
//            int sid = Integer.parseInt(subId);
//            if(sid>0)
//            {
//                thirdIds.add(""+(sid*10));
//                thirdIds.add(""+(sid*10+1));
//                thirdIds.add(""+(sid*10+2));
//            }
//        }

        //采集数据
        genDemoData(dateTime, fromSources, starters, tagIds, subIds, thirdIds);

        //根据采集的数据按照各维度来生成任意维度(包括多维度组合)为全部的汇总数据
        Stat stat =genStat(dateTime, fromSources, starters, tagIds, subIds, thirdIds);

        StatTotalUtil.statTotal(stat,StatSafeguard.class,jdbcTemplate,
            new StatTotalUtil.StatCallBack<StatSafeguard>() {
                public void deal(List<StatSafeguard> perList) {
                    int perCount = (perList==null)?0:perList.size();
                    logger.info("SafeguardStatExecutor genStat perCount="+perCount);
                    if(perCount>0)
                    {
                        Date now = new Date();
                        for(StatSafeguard per:perList)
                        {
                            per.setInsertTime(now);
                        }
                        safeguardStatDao.batchInsert(perList);
                    }
                }
            }
        );

        logger.info("SafeguardStatExecutor exec end,dateTime="+dateTime);
    }

    //产生假数据
    public void genDemoData(DateTime dateTime,List<String> fromSources,List<String> starters,List<String> tagIds,List<String> subIds,List<String> thirdIds)
    {
        List<StatSafeguard> resultList = new ArrayList<StatSafeguard>();
        Date statDate = DateUtil.getDate(dateTime.toString(DateUtil.FMT_YMD),DateUtil.FMT_YMD);
        Date now = new Date();

        for(String fromSource:fromSources)
        {
            if(Integer.parseInt(fromSource)==0)
            {
                continue;
            }
            for(String starter:starters)
            {
                if(Integer.parseInt(starter)==0)
                {
                    continue;
                }
                for(String tagId:tagIds)
                {
                    if(Integer.parseInt(tagId)==0)
                    {
                        continue;
                    }
                    for(String subId:subIds)
                    {
                        if(Integer.parseInt(subId)==0)
                        {
                            continue;
                        }
                        for(String thirdId:thirdIds)
                        {
                            if(Integer.parseInt(thirdId)==0)
                            {
                                continue;
                            }
                            StatSafeguard statSafeguard = new StatSafeguard();
                            statSafeguard.setStatDate(statDate);
                            statSafeguard.setFromSource(Integer.parseInt(fromSource));
                            statSafeguard.setSafeguardStarter(Integer.parseInt(starter));
                            statSafeguard.setTagId(Integer.parseInt(tagId));
                            statSafeguard.setSubId(Integer.parseInt(subId));
                            statSafeguard.setThirdId(Integer.parseInt(thirdId));

                            statSafeguard.setInsertTime(now);
                            int incCnt =RandomUtil.nextRandomInt(10, 100);
                            statSafeguard.setIncCnt(incCnt);
                            int shopMistakeCnt=RandomUtil.nextRandomInt(0,(incCnt*2/3));
                            statSafeguard.setShopMistakeCnt(shopMistakeCnt);
                            statSafeguard.setUserMistakeCnt(RandomUtil.nextRandomInt(0,(incCnt-shopMistakeCnt)));
                            int completeCnt =RandomUtil.nextRandomInt(10,100);
                            statSafeguard.setCompleteCnt(completeCnt);
                            int overWeekCnt = RandomUtil.nextRandomInt(0, (completeCnt / 5));
                            statSafeguard.setCompleteOverWeekCnt(overWeekCnt);
                            statSafeguard.setCloseCnt(RandomUtil.nextRandomInt(5,50));
                            statSafeguard.setDealingTotalCnt(RandomUtil.nextRandomInt(100,500));
                            resultList.add(statSafeguard);
                        }
                    }
                }
            }
        }
        int resultCount = (resultList==null)?0:resultList.size();
        logger.info("SafeguardStatExecutor genDemoData resultCount="+resultCount);
        safeguardStatDao.batchInsert(resultList);
        resultList.clear();
    }

    public Stat genStat(DateTime dateTime,List<String> fromSources,List<String> starters,List<String> tagIds,List<String> subIds,List<String> thirdIds)
    {
        Stat stat = new Stat();
        stat.setStatClazz(StatSafeguard.class);
        Date statDate = DateUtil.getDate(dateTime.toString(DateUtil.FMT_YMD),DateUtil.FMT_YMD);
        stat.setStatDate(statDate);
        stat.setDateName("statDate");

        List<Dimen> dimenList = new LinkedList<Dimen>();
        List<Norm> normList = new LinkedList<Norm>();

        dimenList.add(new Dimen("fromSource",Integer.class,fromSources,"0"));
        dimenList.add(new Dimen("safeguardStarter",Integer.class,starters,"0"));
        dimenList.add(new Dimen("tagId",Integer.class,tagIds,"0"));
        dimenList.add(new Dimen("subId",Integer.class,subIds,"0"));
        dimenList.add(new Dimen("thirdId",Integer.class,thirdIds,"0"));

        normList.add(new Norm("incCnt",Integer.class,Norm.NormType.SUM));
        normList.add(new Norm("shopMistakeCnt",Integer.class,Norm.NormType.SUM));
        normList.add(new Norm("userMistakeCnt",Integer.class,Norm.NormType.SUM));
        normList.add(new Norm("completeCnt",Integer.class,Norm.NormType.SUM));
        normList.add(new Norm("completeOverWeekCnt",Integer.class,Norm.NormType.SUM));
        normList.add(new Norm("closeCnt",Integer.class,Norm.NormType.SUM));
        normList.add(new Norm("dealingTotalCnt",Integer.class,Norm.NormType.SUM));

        stat.setDimenList(dimenList);
        stat.setNormList(normList);

        return stat;
    }
}
