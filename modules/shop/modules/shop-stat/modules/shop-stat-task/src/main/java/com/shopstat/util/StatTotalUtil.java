package com.shopstat.util;

import com.shopstat.model.vo.stat.Dimen;
import com.shopstat.model.vo.stat.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 统计汇总工具类
 */
public class StatTotalUtil {

    private static Logger logger= LoggerFactory.getLogger(StatTotalUtil.class);

    /**
     * 汇总统计
     * @param stat   统计实体
     */
    public static <T> List<T> statTotal(Stat stat,Class<T> clazz)
    {
        List<Dimen> dimenList = stat.getDimenList();
        int n = dimenList.size();
        Map<Integer,Integer> maxMap = new HashMap<Integer,Integer>();
        Map<Integer,Integer> totalOptionMap = new HashMap<Integer,Integer>();
        for(int i=0;i<n;i++)
        {
            Dimen demen = dimenList.get(i);
            maxMap.put(i,demen.getOptions().size());
            totalOptionMap.put(i,getPosition(demen.getOptions(),demen.getTotalOption()));
        }
        //把每个维度可能值的组合查找出来
        List<Integer []> combins =null;
        try {
            combins=StatNfor.nfor(n,maxMap);
        }catch(Exception e)
        {
            logger.error("StatTotalUtil invoke StatNfor.nfor error,",e);
            return null;
        }
        //对组合过过滤
        Iterator<Integer []> it = combins.iterator();
        while(it.hasNext())
        {
            Integer [] per=it.next();
            boolean hasTotal= totalFilter(per,totalOptionMap);
            if(!hasTotal)
            {
                it.remove();
            }
        }
        //遍历组合,进行组合查询汇总
        for(Integer [] cimbin:combins)
        {
            //sql一个自动拼接
            //另一个是自定义
            String sql ="select stat_date,d${fromSource} as from_source,d${safeguardStarter} as safeguard_starter,d${tagId} as tag_id,d${subId} as sub_id,d${third_id} as thirdId, " +
             "%s"+ " where stat_date ='%s' " +
             "group by n${fromSource},n${safeguardStarter},n${tagId},n${subId},n${third_id}";
        }
        return  null;

    }

    public static int getPosition(List<String> list,String value)
    {
        int pos =-1;
        if(value==null || list==null)
        {
            return pos;
        }
        for(int i=0;i<list.size();i++)
        {
            if(value.equals(list.get(i)))
            {
                pos=i;
                break;
            }
        }
        return pos;
    }

    public static boolean totalFilter(Integer [] combin,Map<Integer,Integer> totalMap)
    {
        boolean hasTotal=false;
        int n =combin.length;
        for(int i=0;i<n;i++)
        {
            if(totalMap.get(i)!=null && totalMap.get(i)==combin[i].intValue())
            {
                hasTotal=true;
                break;
            }
        }
        return hasTotal;
    }
}
