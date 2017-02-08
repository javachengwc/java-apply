package com.util.base;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 多分割符分割字符串成数组
 */
public class StringSplitUtil {

    /**
     * @param text
     * @param splits
     * @return
     */
    public static String [] split(String text,String ... splits)
    {
        if(StringUtils.isBlank(text))
        {
            return new String[0];
        }
        if(splits==null || splits.length<=0)
        {
            return new String[]{text};
        }
        return split(new String[]{text},splits);
    }

    /**
     * 中间递归方法
     * @param texts
     * @param splits
     * @return
     */
    public static String [] split(String [] texts,String... splits )
    {
        if(splits==null || splits.length<=0)
        {
            return texts;
        }
        List<String> list = new ArrayList<String>();
        String curSplit= splits[0];
        for(String per:texts)
        {
            String tmps [] = splitSingle(per,curSplit);
            if(tmps!=null && tmps.length>0)
            {
                list.addAll(Arrays.asList(tmps));
            }
        }
        String [] nextTexts = list.toArray(new String[list.size()]);
        String [] nextSplits=getSubArrayNoFirst(splits);
        return split(nextTexts,nextSplits);

    }

    /**
     * 单拆方法
     * @param text
     * @param split
     * @return
     */
    public static String [] splitSingle(String text,String split)
    {
        if(StringUtils.isBlank(text))
        {
            return new String[0];
        }
        if(split==null || "".equals(split))
        {
            return new String[] {text};
        }
        return text.split(split);
    }

    /**
     * 获取数组中排除第一个剩下的元素数组
     * @param arrays
     * @return
     */
    public static String [] getSubArrayNoFirst(String ...arrays)
    {
        if(arrays==null || arrays.length<=1)
        {
            return null;
        }
        int size = arrays.length-1;
        String result [] = new String[size];
        for(int i=1;i<=size;i++)
        {
            result[i-1]=arrays[i];
        }
        return result;
    }


    public static void main(String args[])
    {
        String text="你好；happy new year,what，noting。哈哈";

        String [] result = split(text,"；",",","，","。");

        System.out.println("---------------------------");
        for(String per:result) {
            System.out.println(per);
        }
        System.out.println("---------------------------");
    }
}
