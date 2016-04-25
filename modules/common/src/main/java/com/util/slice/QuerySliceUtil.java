package com.util.slice;

import java.util.ArrayList;
import java.util.List;

/**
 * 分片查询工具
 */
public class QuerySliceUtil {

    private static int DEF_PER=10;

    public static  <T> List<T> query(QueryPage<T> queryPage,int perSize)
    {
        List<T> all = new ArrayList<T>();

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

                all.addAll(perList);
                perList.clear();
            }
        }
        return all;
    }

    public interface QueryPage<T>
    {
        public List<T> queryPage(int start, int perSize);
    }
}
