package com.util;

public class HashUtil {

    /**
     * 根据mod 进行hash
     */
    public static int hash(String s, int mod)
    {
        int code = s.hashCode();

        return (code % mod);
    }

    public static int hash2(String s, int mod)
    {
        long hash = 5381;
        for(int i = 0; i < s.length(); i++)
        {
            hash = ((hash << 5) + hash) + s.charAt(i);
            hash = hash & 0xFFFFFFFFl;
        }
        return (int)(hash % mod);
    }
}
