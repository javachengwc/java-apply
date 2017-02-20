package com.util.base;

import com.util.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class ObjectUtil {

    private static Long zeroLong =0l;

    //转成String
    public static String o2Str(Object obj) {

        return (obj==null)?null:obj.toString();
    }

    //转成char
    public static Character o2Char(Object obj)
    {
        if(obj==null)
        {
            return null;
        }
        String objStr =obj.toString();
        Character rt= objStr.toCharArray()[0];
        return rt;
    }

    //转成boolean
    public static boolean o2Boolean(Object obj) {

        if(obj==null)
        {
            return false;
        }
        String objStr =obj.toString();
        return Boolean.parseBoolean(objStr);
    }

    //转成Byte
    public static Byte o2Byte(Object obj) {
        return o2Byte(obj, null);
    }

    public static Byte o2Byte(Object obj,Integer defNum) {
        Byte rt= (defNum==null)?null:defNum.byteValue();
        if(obj==null)
        {
            return rt;
        }
        String objStr = obj.toString();
        if(!StringUtils.isBlank(objStr))
        {
            rt=Byte.parseByte(objStr);
        }
        return rt;
    }

    //转成Short
    public static Short o2Short(Object obj) {
        return o2Short(obj,null);
    }

    public static Short o2Short(Object obj,Integer defNum) {
        Short rt= (defNum==null)?null:defNum.shortValue();
        if(obj==null)
        {
            return rt;
        }
        String objStr = obj.toString();
        if(NumberUtil.isNumeric(objStr))
        {
            rt=Short.parseShort(objStr);
        }else if(NumberUtil.isFloatNumeric(objStr))
        {
            rt =new Double(Double.parseDouble(objStr)).shortValue();
        }
        return rt;
    }

    //转成Integer
    public static Integer o2Integer(Object obj) {

        return o2Integer(obj,null);
    }

    public static Integer o2Integer(Object obj,Integer defNum) {
        Integer rt= defNum;
        if(obj==null)
        {
            return rt;
        }
        String objStr = obj.toString();
        if(NumberUtil.isNumeric(objStr))
        {
            rt =Integer.parseInt(objStr);
        }else if(NumberUtil.isFloatNumeric(objStr))
        {
            rt =new Double(Double.parseDouble(objStr)).intValue();
        }
        return rt;
    }

    //转成Long
    public static Long o2Long(Object obj) {

        return o2Long(obj, null);
    }

    public static Long o2Long(Object obj,Long defNum) {
        Long rt= defNum;
        if(obj==null)
        {
            return rt;
        }
        String objStr = obj.toString();
        if(NumberUtil.isNumeric(objStr))
        {
            rt =Long.parseLong(objStr);
        }else if(NumberUtil.isFloatNumeric(objStr))
        {
            rt =new Double(Double.parseDouble(objStr)).longValue();
        }
        return rt;
    }

    //转成Float
    public static Float o2Float(Object obj){
        return o2Float(obj,null);
    }

    public static Float o2Float(Object obj,Integer defNum)
    {
        Float rt= (defNum==null)?null:defNum.floatValue();
        if(obj==null)
        {
            return rt;
        }
        String objStr = obj.toString();
        if(NumberUtil.isFloatNumeric(objStr) || NumberUtil.isNumeric(objStr))
        {
            rt=Float.parseFloat(objStr);
        }
        return rt;
    }

    //转成Double
    public static Double o2Double(Object obj){
        return o2Double(obj, null);
    }

    public static Double o2Double(Object obj,Integer defNum) {
        Double rt= (defNum==null)?null:defNum.doubleValue();
        if(obj==null)
        {
            return rt;
        }
        String objStr = obj.toString();
        if(NumberUtil.isFloatNumeric(objStr) || NumberUtil.isNumeric(objStr))
        {
            rt=Double.parseDouble(objStr);
        }
        return rt;
    }

    //转成BigDecimal
    public static BigDecimal o2BigDecimal(Object obj)
    {
        return o2BigDecimal(obj,null);
    }
    public static BigDecimal o2BigDecimal(Object obj,Integer defNum) {
        BigDecimal df= (defNum==null)?null:BigDecimal.valueOf(defNum.longValue());
        if(obj==null)
        {
            return df;
        }
        if(obj instanceof BigDecimal)
        {
            return (BigDecimal) obj;
        }
        String objStr = obj.toString();
        if(NumberUtil.isNumeric(objStr))
        {
            Long rt =Long.parseLong(objStr);
            return BigDecimal.valueOf(rt);
        }
        if(NumberUtil.isFloatNumeric(objStr))
        {
            Double rt =Double.parseDouble(objStr);
            return BigDecimal.valueOf(rt);
        }
        return df;
    }

    //转成Date
    public static Date o2Date(Object obj) {
        if(obj==null)
        {
            return null;
        }
        if(obj instanceof Date)
        {
            return (Date)obj;
        }
        if(obj instanceof Timestamp)
        {
            Timestamp dateTime =(Timestamp)obj;
            if (dateTime != null) {
                return new Date(dateTime.getTime());
            }
        }
        String objStr = obj.toString();
        int strLen =objStr.length();
        if(strLen==DateUtil.FMT_YMD_HMS.length() &&  DateUtil.checkDate(objStr,DateUtil.FMT_YMD_HMS))
        {
           return DateUtil.getDate(objStr,DateUtil.FMT_YMD_HMS);
        }
        if(strLen==DateUtil.FMT_YMD.length()  && DateUtil.checkDate(objStr,DateUtil.FMT_YMD))
        {
            return DateUtil.getDate(objStr,DateUtil.FMT_YMD);
        }
        if(strLen==DateUtil.FMT_YMD_HMS_MS.length() &&  DateUtil.checkDate(objStr,DateUtil.FMT_YMD_HMS_MS))
        {
            return DateUtil.getDate(objStr,DateUtil.FMT_YMD_HMS_MS);
        }
        return null;
    }


    public static Object o2Data(Object obj ,Class clazz,Integer defNum)
    {
        if(clazz==String.class)
        {
            return o2Str(obj);
        }
        if(clazz==boolean.class || clazz==Boolean.class)
        {
            boolean rt= o2Boolean(obj);
            return (clazz==boolean.class)?rt:Boolean.valueOf(rt);
        }
        if(clazz==int.class || clazz==Integer.class)
        {
            return numberSerialize(o2Integer(obj,defNum),clazz);
        }
        if(clazz==long.class || clazz==Long.class)
        {
            Long defLong = (defNum==null)?null:defNum.longValue();
            return numberSerialize(o2Long(obj,defLong),clazz);
        }
        if(clazz==float.class || clazz==Float.class)
        {
            return numberSerialize(o2Float(obj,defNum),clazz);
        }
        if(clazz==double.class || clazz==Double.class)
        {
            return numberSerialize(o2Double(obj,defNum),clazz);
        }
        if(clazz==byte.class || clazz==Byte.class )
        {
            return numberSerialize(o2Byte(obj,defNum),clazz);
        }
        if(clazz==short.class || clazz==Short.class)
        {
            return numberSerialize(o2Short(obj,defNum),clazz);
        }
        if(clazz==char.class || clazz==Character.class)
        {
            Character rt= o2Char(obj);
            if(clazz==char.class && rt==null)
            {
                rt =zeroLong.toString().toCharArray()[0];
            }
            return rt;
        }
        if(clazz==Date.class)
        {
            return o2Date(obj);
        }
        if(clazz==BigDecimal.class)
        {
            return o2BigDecimal(obj,defNum);
        }
        return obj;
    }

    public static Object numberSerialize(Number number ,Class clazz)
    {
        if(number!=null)
        {
            return number;
        }
        if(clazz==int.class)
        {
            return zeroLong.intValue();
        }
        if(clazz==byte.class)
        {
            return zeroLong.byteValue();
        }
        if(clazz==short.class)
        {
            return zeroLong.shortValue();
        }
        if(clazz==long.class)
        {
            return zeroLong.longValue();
        }
        if(clazz==float.class)
        {
            return zeroLong.floatValue();
        }
        if(clazz==double.class)
        {
            return zeroLong.doubleValue();
        }
        return number;
    }

}
