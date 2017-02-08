package com.arithmetic.other;

import com.util.enh.DeepCopyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 带额外条件的嵌套的N层循环
 */
public class Nfor2 {

    public static List<Integer []> nfor(int n,Map<Integer,Integer> limitMaxMap) throws Exception
    {
        return nfor(n,n,limitMaxMap);
    }

    /**
     * @param n n进制
     * @param len 位数
     * @param limitMaxMap 对每一位数最大值限制
     */
    public static List<Integer []> nfor(int n,int len,Map<Integer,Integer> limitMaxMap) throws Exception
    {
        List<Integer []> rtList =new ArrayList<Integer []>();

        Integer [] as = new Integer [len];
        for (int i = 0; i < len; i++) {
            as[i] = 0;
        }
        rtList.add((Integer []) DeepCopyUtil.deepCopy(as));
        printArray(as);

        int zeroMax= (limitMaxMap.get(0)==null)?(n-1):limitMaxMap.get(0);
        while (as[0] <= zeroMax) {
            as[len-1]++;    //最后一层的循环
            for (int i = len - 1; i > 0; i--) {
                int nMax= (limitMaxMap.get(i)==null)?(n-1):limitMaxMap.get(i);
                if (as[i] > nMax) {   //n为第i层循环中，i的终值
                    as[i-1]++;
                    as[i] = 0;
                }
            }
            if(as[0] <= zeroMax)
            {
                rtList.add((Integer [])DeepCopyUtil.deepCopy(as));
                printArray(as);
            }
        }
        System.out.println("-------total count:"+rtList.size());

        return rtList;
    }

    public static void main(String args []) throws  Exception
    {
        Map<Integer,Integer> limitMaxMap =new HashMap<Integer,Integer>();
        limitMaxMap.put(0,2);
        limitMaxMap.put(1,3);
        limitMaxMap.put(2,2);
        List<Integer []> fList = nfor(6,3,limitMaxMap);

        System.out.println(fList.size());

        for(Integer [] per:fList)
        {
            printArray(per);
        }
        System.out.println(fList.size());
    }

    public static void printArray( Integer[] as)
    {
        for(Integer i:as)
        {
            System.out.print(i+" ");
        }
        System.out.println();
    }
}
