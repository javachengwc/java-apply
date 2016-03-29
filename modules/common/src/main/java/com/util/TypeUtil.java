package com.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class TypeUtil {
	
	
    public static Type[] getTypeParams(Class<?> klass) {   
	    if (klass == null || "java.lang.Object".equals(klass.getName()))   
	        return null;   
	    // 看看父类   
	    Type superclass = klass.getGenericSuperclass();   
	   if (null != superclass && superclass instanceof ParameterizedType)   
	        return ((ParameterizedType) superclass).getActualTypeArguments();   
	  
	   // 看看接口   
	    Type[] interfaces = klass.getGenericInterfaces();   
	   for (Type inf : interfaces) {   
	       if (inf instanceof ParameterizedType) {   
	            return ((ParameterizedType) inf).getActualTypeArguments();   
	        }   
	    }   
	    return getTypeParams(klass.getSuperclass());   
    }  
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
         int pos = i * 2;
         result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    
    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    
    public static   String byteArrayToBinaryString(byte[] bt)   {
        StringBuffer   sb   =   new   StringBuffer();
        for(int j=0; j<bt.length; j++){
        	for   (int   i   =   7;   i   >=0;   i--)   {
                sb.append(((bt[j]&(1 <<i))!=0)? '1': '0');
            }
        }
        return   sb.toString();
    }
    
    public static String hexStringToBinaryString(String hexString){
    	return TypeUtil.byteArrayToBinaryString(TypeUtil.hexStringToByte(hexString));
    }
    
    public static int getQDCount(String binaryString){
		String binaryStrQDCount = binaryString.substring(32, 48);
		System.out.println(binaryStrQDCount);
		Integer integer = Integer.valueOf(binaryStrQDCount, 2);
		return integer.intValue();
	}
    
    public static int getANCount(String binaryString){
		String binaryStrQDCount = binaryString.substring(48, 64);
		System.out.println(binaryStrQDCount);
		Integer integer = Integer.valueOf(binaryStrQDCount, 2);
		return integer.intValue();
	}
   
    public static List<String> getDNSIp(String binaryString){
    	int qdCount = TypeUtil.getQDCount(binaryString);
    	int anCount = TypeUtil.getANCount(binaryString);
    	
    	
    	return null;
    }
    public static Integer bigDecimalToInteger(BigDecimal b) {
        try {
            if (b == null)
            return null;
            return b.intValue();
        } catch (Exception e) {
            return null;

        }
            
    }

    public static Character objectToChar(Object o) {
    	try {
            if (o == null)
                return null;
            String t = o.toString();
            if ("".equals(t))
                return null;
            return t.toCharArray()[0];
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String objectToString(Object o) {
        try {
            if (o == null)
                return null;
            return o.toString();
        } catch (Exception e) {
            return null;

        }
    }
    
    public static Boolean objectToBoolean(Object o) {
    	try {
            if (o == null)
                return false;
            String t = o.toString();
            if ("".equals(t))
                return false;
            return Boolean.parseBoolean(t);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static Short objectToShort(Object o) {
        try {
            if (o == null)
                return null;
            String t = o.toString();
            if ("".equals(t))
                return null;
            return new Integer(t).shortValue();
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Float objectToFloat(Object o) {
        try {
            if (o == null)
                return null;
            String t = o.toString();
            if ("".equals(t))
                return null;
            return Float.parseFloat(t);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Integer objectToInteger(Object o) {
        try {
            if (o == null)
                return null;
            String t = o.toString();
            if ("".equals(t))
                return null;
            return new Integer(t);
        } catch (Exception e) {
            return null;
        }
    }
    
	public static Long objectToLong(Object o) {
		try {
			if (o == null)
				return null;
			String t = o.toString();
			if ("".equals(t))
				return null;
			return new Long(t);
		} catch (Exception e) {
			return null;
		}
	}

    public static Double objectToDouble(Object o) {
		try {
			if (o == null)
				return null;
			String t = o.toString();
			if ("".equals(t))
				return null;
			return new Double(t);
		} catch (Exception e) {
			return null;
		}
	}

    public static BigDecimal objectToBigDecimal(Object o) {
        try {
            if (o == null)
                return null;
            Integer t = new Integer(o.toString());
            return new BigDecimal(t);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static BigDecimal shortToBigDecimal(Short o) {
		try {
			if (o == null)
				return null;
			Integer t = new Integer(o.toString());
			return new BigDecimal(t);
		} catch (Exception e) {
			return null;
		}
	}
    
    public static BigDecimal stringToBigDecimal(String o) {
        try {
            if (o == null)
                return null;
            Integer t = new Integer(o);
            return new BigDecimal(t);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Byte objectToByte(Object o) {
		try {
			if (o == null)
				return null;
			return Byte.parseByte(o.toString());
		} catch (Exception e) {
			return null;
		}
	}

    public static String bigDecimalToString(BigDecimal from) {
        try {
            if (from == null)
                return null;
            return from.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static Date objectToDate(Object o) {
        try {
            if (o == null)
                return null;
            return (Date) o;
        } catch (Exception e) {
            return null;
        }
    }
    public static Timestamp objectToTimstamp(Object o) {
        try {
            if (o == null)
                return null;
            return (Timestamp) o;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Date timstampToDate(Object o) {
        try {
            if (o == null)
                return null;
            return (Timestamp) o;
        } catch (Exception e) {
            return null;
        }
    }

}
