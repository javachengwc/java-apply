package com.other.guava;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionMain {

    public static void main(String args [])
    {
        Set<Object> sets = new HashSet<Object>();
        Object o = new Object();
        sets.add(o);
        sets.add(o);
        sets.add(o);

        System.out.println(sets.size());

        List<Set<String>> listSet = new ArrayList<Set<String>>();
        Set<String> set1= new HashSet<String>();
        set1.add("1");set1.add("2");set1.add("3");
        Set<String> set2= new HashSet<String>();
        set2.add("8");set2.add("9");set2.add("10");
        Set<String> set3= new HashSet<String>();
        set3.add("5");set3.add("6");set3.add("7");
        listSet.add(set1);listSet.add(set2);listSet.add(set3);

        System.out.println("----------listSet-------------");
        System.out.println(listSet2Str(listSet));

        //笛卡尔积组合==各维度值组合
        Set<List<String>> setList =Sets.cartesianProduct(listSet);

        System.out.println("----------setList-------------");
        System.out.println(setList2Str(setList));
    }

    public static String listSet2Str(List<Set<String>> list)
    {
        int count = (list==null)?0:list.size();
        if(count<=0)
        {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        for(Set<String> perSet:list)
        {
            buf.append("[");
            if(perSet!=null && perSet.size()>0)
            {
                for(String per:perSet)
                {
                    buf.append(per).append(",");
                }
            }
            buf.append("],");
        }
        return buf.toString();
    }

    public static String setList2Str(Set<List<String>> list)
    {
        int count = (list==null)?0:list.size();
        if(count<=0)
        {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        for(List<String> perList:list)
        {
            buf.append("[");
            if(perList!=null && perList.size()>0)
            {
                for(String per:perList)
                {
                    buf.append(per).append(",");
                }
            }
            buf.append("],");
        }
        return buf.toString();
    }
}
