package com.util.base;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.util.base.BlankUtil;
import org.apache.commons.lang3.StringUtils;

public final class NumberUtil {
	
	//数字正则表达
	public static Pattern numericPattern = Pattern.compile("[-+]?[0-9]+");
    private static Pattern floatNumericPattern = Pattern.compile("^[0-9\\-\\.]+$");

	public static boolean isNumeric(String str){
		if(StringUtils.isBlank(str)){
			return false;
		}
		Matcher isNum = numericPattern.matcher(str);
		if( !isNum.matches() )
		{
			return false;
		}else{
			return true;
		} 
	}

    /**
     * 判断字符串数组是否都是数字
     * @param str
     * @return
     */
    public static boolean isAllNumeric(String ...str)
    {
        if(BlankUtil.isBlank(str) || str.length<=0)
        {
            return false;
        }
        boolean result =true;
        for(String perStr:str)
        {
            if(!isNumeric(perStr))
            {
                result =false;
                break;
            }
        }
        return result;
    }

    /**
     * 判断是否浮点数字表示
     */
    public static boolean isFloatNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = floatNumericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    /**
     * 带小数位的除法
     * @param a             除数
     * @param divideValue   被除数
     * @param keep          结果保留小数位位数
     * @return
     */
    public static String getDivide(Number a,Number divideValue,int keep)
    {

        if(a==null || divideValue==null )
        {
            return "";
        }
        if(divideValue.intValue()==0)
        {
            return "";
        }

        double percent = a.doubleValue() /divideValue.doubleValue();
        BigDecimal bd = new BigDecimal("" + percent);
        bd = bd.setScale(keep, BigDecimal.ROUND_HALF_UP);
        String rt= "" + bd;
        Pattern p = Pattern.compile("\\.0{"+keep+"}");
        Matcher m = p.matcher(rt);
        if(m.find())
        {
            rt =rt.replace(m.group(),"");
        }
        return rt;
    }

    public static String getDivide100(Number data){
        String result = "";
        if (data == null){
            return result;
        } else {
            result = String.format("%.2f", data.doubleValue()/100.0);
        }
        return result;
    }

    public static String formatToStr(Number data,DecimalFormat format)
    {
        //DecimalFormat format = new DecimalFormat( "#0.00");
        return format.format(data);
    }

    /**
     * 转换成可读的千位符数据
     * @param number
     * @param needTrans
     * @return
     */
    public static String transKilobitData(Long number,boolean needTrans)
    {
        String data=(number==null)?"":number.toString();
        if(needTrans) {
            data = getDivide(number, 100, 2);
        }
        if(StringUtils.isBlank(data))
        {
            return data;
        }
        int idx = data.indexOf(".");
        int len =data.length();
        if(idx>0)
        {
            len = data.substring(0,idx).length();
        }

        int times =len/3;
        if((len%3)==0)
        {
            times-=1;
        }
        if(times <=0)
        {
            return data;
        }

        StringBuffer buf = new StringBuffer("");

        int end=0;
        for(int i=times;i>0;i--)
        {
            end = len -(i*3);
            int start =end-3;
            if(start<0)
            {
                start=0;
            };

            buf.append(data.substring(start,end)).append(",");
        }

        if(end<data.length() ) {
            buf.append(data.substring(end));
        }

        String rt =buf.toString();
        rt = rt.replace("-,","-");

        return rt;
    }

    /**
     * DecimalFormat实现千位符
     * 比如:parseMoney(-8850002210.03,"###,###.##")
     */
    public static  String parseMoney(Number amount,String pattern){
        DecimalFormat df=new DecimalFormat(pattern);
        return df.format(amount);
    }

    public static void main(String args[])
    {

        System.out.println(getDivide100(25));
        System.out.println(getDivide(25,3,2));

        DecimalFormat format = new DecimalFormat( "#0.00");
        System.out.println(formatToStr(500.2,format) );

        System.out.println(transKilobitData(-885000221003l,true));

        System.out.println(parseMoney(8850002210.03,"###,###.##"));
    }
}
