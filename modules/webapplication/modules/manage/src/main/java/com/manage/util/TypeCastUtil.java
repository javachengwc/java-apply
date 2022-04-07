package com.manage.util;

import java.io.BufferedReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


/**
 * 通用的类型转换
 */
public final class TypeCastUtil {
    public static final String LONG_DATE_FORMAT = "dd-MM-yyyy";
    
    public static Integer BigDecimalToInteger(BigDecimal b) {
        try {
            if (b == null)
            return null;
            return b.intValue();
        } catch (Exception e) {
            return null;

        }
            
    }
    public static Long BigDecimalToLong(BigDecimal b) {
        try {
            if (b == null)
            return null;
            return b.longValue();
        } catch (Exception e) {
            return null;

        }
            
    }
    public static String ObjectToString(Object o) {
        try {
            if (o == null)
                return null;
            return o.toString();
        } catch (Exception e) {
            return null;

        }
    }

    public static Short ObjectToShort(Object o) {
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
    
    public static Integer ObjectToInteger(Object o) {
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
    
	public static Long ObjectToLong(Object o) {
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

    public static Double ObjectToDouble(Object o) {
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

    public static BigDecimal ObjectToBigDecimal(Object o) {
        try {
            if (o == null)
                return null;
            Integer t = new Integer(o.toString());
            return new BigDecimal(t);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static BigDecimal ShortToBigDecimal(Short o) {
		try {
			if (o == null)
				return null;
			Integer t = new Integer(o.toString());
			return new BigDecimal(t);
		} catch (Exception e) {
			return null;
		}
	}
    
    public static BigDecimal StringToBigDecimal(String o) {
        try {
            if (o == null)
                return null;
            Integer t = new Integer(o);
            return new BigDecimal(t);
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Byte ObjectToByte(Object o) {
		try {
			if (o == null)
				return null;
			return Byte.parseByte(o.toString());
		} catch (Exception e) {
			return null;
		}
	}

    public static String BigDecimalToString(BigDecimal from) {
        try {
            if (from == null)
                return null;
            return from.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static Date ObjectToDate(Object o) {
        try {
            if (o == null)
                return null;
            return (Date) o;
        } catch (Exception e) {
            return null;
        }
    }
    public static Timestamp ObjectToTimstamp(Object o) {
        try {
            if (o == null)
                return null;
            return (Timestamp) o;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static Date TimstampToDate(Object o) {
        try {
            if (o == null)
                return null;
            return (Timestamp) o;
        } catch (Exception e) {
            return null;
        }
    }
    
    
    
    
    
    public static boolean isValidatedString(String str) {
		if (null == str)
			return false;
		if ("".equals(str))
			return false;
		if ("- Select -".equals(str))
			return false;
		if ("-1".equals(str))
			return false;
		if ("- All -".equals(str))
            return false;
		return true;
	}
	
	   public static boolean validateEmail(String emailAddress) {
        if (emailAddress == null)
            return false;
        String emailRegex = "(\\w+(\\.\\w+)*@\\w+(\\.\\w+)+)";
        return Pattern.matches(emailRegex, emailAddress);

    }
    
    
    
    public static String DateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		// 把日期转换为字符串
        String str = null;
        if (date != null) {
          try {
            str= sdf.format(date);
        } catch (Exception e) {
            str = null;
        }
      } else {
          str = null;
      }
        return str;

    }
    
    public static String DateToStringNew(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy");
		// 把日期转换为字符串
        String str = null;
        if (date != null) {
            try {
                str = sdf.format(date);
            } catch (Exception e) {
                str = null;
            }
        } else {
            str = null;
        }
        return str;

    }
    
    public static String DateToFullString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 把日期转换为字符串
        String str = null;
        if (date != null) {
            try {
                str = sdf.format(date);
            } catch (Exception e) {
                str = null;
            }
        } else {
            str = null;
        }
        return str;

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
    	return TypeCastUtil.byteArrayToBinaryString(TypeCastUtil.hexStringToByte(hexString));
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
    	int qdCount = TypeCastUtil.getQDCount(binaryString);
    	int anCount = TypeCastUtil.getANCount(binaryString);
    	
    	
    	return null;
    }

    public static void main(String args[]) {
//    	String binaryString = TypeCastUtil.hexStringToBinaryString("4FE5818000010001000000000673746174696306676F75676F7503636F6D0000010001C00C00010001000000A800043AFB394E");
    	System.out.println(TypeCastUtil.DateToFullString(new Date()));
    	
    }
	public static String ClobToString(Clob clob) throws Exception{
		String reString = "";
		 Reader is = clob.getCharacterStream();// 得到流
		 BufferedReader br = new BufferedReader(is);
		 String s = br.readLine();
		 StringBuffer sb = new StringBuffer();
		 while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
		 sb.append(s);
		 s = br.readLine();
		 }
		 reString = sb.toString();
		 return reString;
		 
	}

}
