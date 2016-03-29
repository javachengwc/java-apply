package com.jsonlib;

import net.sf.json.util.PropertyFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 基本类型的json属性过滤器,除了基本类型如Integer,Long,String...之外,不参加序列化
 */
public class PrimitiveJsonFilter implements PropertyFilter{

    public static  List<String> serializeClassList = new ArrayList<String>();

    static{
        String [] classAy=new String [] {"Boolean","Byte","Short","Integer","Float","Long","Double","String","Date","Timestamp","String","BigDecimal","BigInteger"};

        serializeClassList= Arrays.asList(classAy);
    }

	public boolean apply(Object source, String name, Object value) {
        //返回false才被序列化到json
        if (value == null) {
            return true;
        }
        if (value.getClass().isPrimitive()) {
            return false;
        }
        if (value.getClass().isEnum()) {
            return false;
        }
        String simpleName = value.getClass().getSimpleName();

        if (serializeClassList.contains(simpleName)) {
            return false;
        }
        return true;
    }
}
