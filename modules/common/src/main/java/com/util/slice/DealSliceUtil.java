package com.util.slice;

import java.util.List;

/**
 * 分片处理工具
 */
public class DealSliceUtil {

    private static int DEF_PER=100;

    public static <T> void deal(DealUnit<T> dealUnit,List<T> list, int perCount)
    {
        int total = (list==null)?0:list.size();
        if(perCount<=0)
        {
            perCount=DEF_PER;
        }
        int times = total/perCount;
        if((total%perCount)>0 )
        {
            times+=1;
        }
        for(int i=0;i<times;i++)
        {
            int start = i*perCount;
            int end = (i+1)*perCount;
            if((i+1)>=times)
            {
                end=total;
            }
            List<T> perList = list.subList(start,end);
            dealUnit.deal(perList);
        }
    }

    public static <T>  void queryDeal(QuerySliceUtil.QueryPage<T> queryPage,DealUnit<T> dealUnit,int perSize,int dealPerCount)
    {
        int start =0;
        if(perSize<=0)
        {
            perSize=DEF_PER;
        }
        int queryCount=perSize;

        while(queryCount>=perSize)
        {
            List<T> perList = queryPage.queryPage(start,perSize);
            queryCount = (perList==null)?0:perList.size();

            if(queryCount>0)
            {
                start=start+queryCount;

                DealSliceUtil.deal(dealUnit,perList,dealPerCount);

                perList.clear();
            }
        }
    }

    public interface DealUnit<T>
    {
        public void deal(List<T> perList);
    }
}
