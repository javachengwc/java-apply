package com.tool.util.base;

import com.tool.util.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class ObjectUtil {

    private static Long zeroLong =0l;

    //转成String
    public static String obj2Str(Object obj) {

        return (obj==null)?null:obj.toString();
    }

    //转成char
    public static Character obj2Char(Object obj)
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
    public static boolean obj2Boolean(Object obj) {

        if(obj==null)
        {
            return false;
        }
        String objStr =obj.toString();
        return Boolean.parseBoolean(objStr);
    }

    //转成Byte
    public static Byte obj2Byte(Object obj) {
        return obj2Byte(obj, null);
    }

    public static Byte obj2Byte(Object obj,Integer defNum) {
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

    public static byte[] obj2ByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    //转成Short
    public static Short obj2Short(Object obj) {
        return obj2Short(obj,null);
    }

    public static Short obj2Short(Object obj,Integer defNum) {
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
    public static Integer obj2Integer(Object obj) {

        return obj2Integer(obj,null);
    }

    public static Integer obj2Integer(Object obj,Integer defNum) {
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
    public static Long obj2Long(Object obj) {

        return obj2Long(obj, null);
    }

    public static Long obj2Long(Object obj,Long defNum) {
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
    public static Float obj2Float(Object obj){
        return obj2Float(obj,null);
    }

    public static Float obj2Float(Object obj,Integer defNum)
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
    public static Double obj2Double(Object obj){
        return obj2Double(obj, null);
    }

    public static Double obj2Double(Object obj,Integer defNum) {
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
    public static BigDecimal obj2BigDecimal(Object obj)
    {
        return obj2BigDecimal(obj,null);
    }
    public static BigDecimal obj2BigDecimal(Object obj,Integer defNum) {
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
    public static Date obj2Date(Object obj) {
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


    public static Object obj2Data(Object obj ,Class clazz,Integer defNum)
    {
        if(clazz==String.class)
        {
            return obj2Str(obj);
        }
        if(clazz==boolean.class || clazz==Boolean.class)
        {
            boolean rt= obj2Boolean(obj);
            return (clazz==boolean.class)?rt:Boolean.valueOf(rt);
        }
        if(clazz==int.class || clazz==Integer.class)
        {
            return numberSerialize(obj2Integer(obj,defNum),clazz);
        }
        if(clazz==long.class || clazz==Long.class)
        {
            Long defLong = (defNum==null)?null:defNum.longValue();
            return numberSerialize(obj2Long(obj,defLong),clazz);
        }
        if(clazz==float.class || clazz==Float.class)
        {
            return numberSerialize(obj2Float(obj,defNum),clazz);
        }
        if(clazz==double.class || clazz==Double.class)
        {
            return numberSerialize(obj2Double(obj,defNum),clazz);
        }
        if(clazz==byte.class || clazz==Byte.class )
        {
            return numberSerialize(obj2Byte(obj,defNum),clazz);
        }
        if(clazz==short.class || clazz==Short.class)
        {
            return numberSerialize(obj2Short(obj,defNum),clazz);
        }
        if(clazz==char.class || clazz==Character.class)
        {
            Character rt= obj2Char(obj);
            if(clazz==char.class && rt==null)
            {
                rt =zeroLong.toString().toCharArray()[0];
            }
            return rt;
        }
        if(clazz==Date.class)
        {
            return obj2Date(obj);
        }
        if(clazz==BigDecimal.class)
        {
            return obj2BigDecimal(obj,defNum);
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
