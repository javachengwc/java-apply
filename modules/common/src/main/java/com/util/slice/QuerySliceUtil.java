package com.util.slice;

import java.util.ArrayList;
import java.util.List;

/**
 * 分片查询工具
 */
public class QuerySliceUtil {

    private static int DEF_PER=10;

    public static  <T>  List<T> query(QueryPage<T> queryPage,int perSize)
    {
        return query(queryPage,0,perSize);
    }

    public static  <T>  List<T> query(QueryPage<T> queryPage,int startNo ,int perSize)
    {
        List<T> all = new ArrayList<T>();
        if(perSize<=0)
        {
            perSize=DEF_PER;
        }
        int start =startNo;
        int queryCount=perSize;

        while(queryCount>=perSize)
        {
            List<T> perList = queryPage.queryPage(start,perSize);
            queryCount = (perList==null)?0:perList.size();

            if(queryCount>0)
            {
                start=start+queryCount;

                all.addAll(perList);
                perList.clear();
            }
        }
        return all;
    }

    public static  <T>  List<T> query(QueryPage<T> queryPage,int startNo,int total ,int perSize)
    {
        List<T> all = new ArrayList<T>();
        if(perSize<=0)
        {
            perSize=DEF_PER;
        }
        int start =startNo;
        int queryCount=perSize;

        int queryTotal=0;
        while(queryCount>=perSize && queryTotal<total)
        {
            int hasCount =total-queryTotal;
            if( hasCount<perSize)
            {
                perSize=hasCount;
            }
            List<T> perList = queryPage.queryPage(start,perSize);
            queryCount = (perList==null)?0:perList.size();

            if(queryCount>0)
            {
                start=start+queryCount;
                queryTotal=queryTotal+queryCount;
                all.addAll(perList);
                perList.clear();
            }
        }
        return all;
    }

    public static <T,I> List<T> query(QueryUnit<T,I> queryUnit,List<I> paramIns,int perSize)
    {
        int count= (paramIns==null)?0:paramIns.size();
        if(count<=0)
        {
            return null;
        }
        int times = count/perSize;
        if((count%perSize)>0)
        {
            times++;
        }
        List<T> rtList = new ArrayList<T>();
        for(int i=0;i<times;i++) {

            int start = i*perSize;
            int end = (i+1)*perSize;
            if((i+1)>=times)
            {
                end=count;
            }
            List<I> per = paramIns.subList(start,end);
            List<T> list =queryUnit.queryUnit(per);
            if(list!=null && list.size()>0)
            {
                rtList.addAll(list);
                list.clear();
            }
        }
        return rtList;
    }

    public interface  QueryUnit<T,I>
    {
        public List<T> queryUnit(List<I> paramIns);
    }

    public interface QueryPage<T>
    {
        public List<T> queryPage(int start, int perSize);
    }
}
