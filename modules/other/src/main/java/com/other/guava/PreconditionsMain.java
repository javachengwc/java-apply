package com.other.guava;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Preconditions检验参数
 *    checkArgument(boolean) ：
 *　　检查boolean是否为真。
 *　　失败时抛出的异常类型: IllegalArgumentException
 *
 *　　checkNotNull(T)：
 *　　检查value不为null， 直接返回value。
 *　　失败时抛出的异常类型：NullPointerException
 *
 * 　　checkState(boolean)：
 * 　　检查对象的一些状态。
 * 　　失败时抛出的异常类型：IllegalStateException
 */
public class PreconditionsMain {

    public static void main(String args []) throws Exception
    {
        try {
            Preconditions.checkArgument(-1>0, "-1>0是错的");
        } catch (Exception e) {
            System.out.println(e.getClass().getName()+" "+e.getMessage());
        }

        try {
            Object o=null;
            Preconditions.checkNotNull(o, "o是空的");
        } catch (Exception e) {
            System.out.println(e.getClass().getName()+" "+e.getMessage());
        }

        List<Integer> intList=new ArrayList<Integer>();
        for(int i=0;i<10;i++){
            try {
                Preconditions.checkState(intList.size() < 9, "intList size 不能大于" + 9);
                intList.add(i);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        int listSize = intList.size();
        System.out.println("intList size="+listSize);
    }
}
