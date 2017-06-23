package com.main;

import java.util.HashMap;
import java.util.Map;

public class EnumMain {

    public static void main(String args [])
    {
        Map<ChannelEnum,Integer> map = new HashMap<ChannelEnum,Integer>();
        map.put(ChannelEnum.Pc,100);
        map.put(ChannelEnum.Mobile,100);

        System.out.println(map);

        System.out.println(map.get(1));
    }
}
