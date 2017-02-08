package com.arithmetic.other;

import com.util.enh.DeepCopyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 嵌套的N层循环
 * 思路:模拟多进制,嵌套的n层就模拟n进制
 */
public class Nfor {

    /**
     * 嵌套n次循环
     * @param n n进制
     */
    public static List<Integer []> nfor(int n) throws Exception
    {
        return nfor(n,n);
    }

    /**
     * @param n n进制
     * @param len 位数
     */
    public static List<Integer []> nfor(int n,int len) throws Exception
    {
        List<Integer []> rtList =new ArrayList<Integer []>();

        Integer [] as = new Integer [len];
        for (int i = 0; i < len; i++) {
            as[i] = 0;
        }
        rtList.add((Integer [])DeepCopyUtil.deepCopy(as));
        printArray(as);

        while (as[0] < n) {
            as[len-1]++;    //最后一层的循环
            for (int i = len - 1; i > 0; i--) {
                if (as[i] >= n) {   //n为第i层循环中，i的终值
                    as[i-1]++;
                    as[i] = 0;
                }
            }
            if(as[0] < n)
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

        List<Integer []> fList = nfor(6,3);

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
