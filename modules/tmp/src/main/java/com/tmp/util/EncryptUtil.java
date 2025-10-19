package com.tmp.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class EncryptUtil extends MD5Util{
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptUtil.class);

    public static String getSign(String s, String token) {

        s = s + token;
        String ret = "";
        try {
            ret = encryptMD5(s);
        } catch (Exception e) {
            LOGGER.error("EncryptUtil getSign exception",e);
        }
        return ret.trim();
    }
    public static String byteXml(String xml) {
        String xmlStr = "";
        xmlStr = new String(StringUtils.getBytesIso8859_1(xml), StandardCharsets.UTF_8);
        return xmlStr;
    }
    public static String sign(Object obj,String token) {
        Map<String, String> map =null;
        try {
            map= BeanUtils.describe(obj);
            if (map == null || map.isEmpty()) {
                return null;
            }
            Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
            sortMap.putAll(map);
            List<String> list=new ArrayList<>();
            for (String key : sortMap.keySet()) {
                Object value = map.get(key);
                if (value == null || key.equals("class")) {
                    continue;
                }
                list.add(key + "=" + value);
            }
            String besing= org.apache.commons.lang3.StringUtils.join(list,"&");
            return getSign(besing+"&",token);
        }catch (Exception e) {
            LOGGER.error("EncryptUtil sign exception",e);
        }
        return null;
    }

    public static class MapKeyComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    }
}

