package com.main;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * string format 转义符是%
 * @author cheng
 *
 */
public class StringMain {

	private static String orderSql="select count(1) from aa where partitiontime='%s' and jumpsource=2 and orderstatus in(2,3,5) and not_register_channel='%s'";

	private static String pvSql="select count(1) from ff %%  where  website='cc.com' and partitiontime='%s%%' and parse_url(concat('http://aa',url),'QUERY','utm_source')='%s'";
	
	private static String uvSql="select count(distinct session_id) uv from  cc where  host='cc.com' and partitiontime='%s'  and  utm_csr='%s'";

    private static  Pattern pattern = Pattern.compile("C\\d{6}0*([1-9]\\d*)");

    private static String  numZimu="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,9}$";

    //${}匹配
    private static String programTemplateStr="\\$\\{(.*?)\\}";

    private static String initMessageTemplateStr="^\\[.*\\](#|\\$)$";

    //去信息
    public static String dropInfo(String value) {
        if (StringUtils.isBlank(value) ) {
            return value;
        }
        int tokenIndex = value.indexOf("\"token\":\"");
        System.out.println(tokenIndex);
        if(tokenIndex<0) {
            return  value;
        }
        tokenIndex+="\"token\":\"".length();
        int afterIndex =value.indexOf("\"",tokenIndex);
        System.out.println(tokenIndex);
        System.out.println(afterIndex);
        System.out.println(value.substring(0,tokenIndex));
        System.out.println(value.substring(afterIndex));
        String rt = value.substring(0,tokenIndex)+value.substring(afterIndex);
        return rt;
    }

    public static void stringSame() {
        String a="aaaaa";
        String b ="aa"+new String("aaa");
        String c="aa"+"aaa";
        boolean same= a==b;
        System.out.println("a==b is "+ same);
        System.out.println("a==c is "+ (a==c));
    }

    public static void main(String args []) throws Exception {


      char charA= 0;

      System.out.println("charA============="+Character.toString(charA));
      String s = new String(new char [] {charA});
      System.out.println("wfhiwhfwihf============="+s);


      String pre1="[fsdhfisdhfi]$";
      String pre2="[fshidfhishdih]#";
      String pre3="[dhfiwhsifhiw]#22";
      System.out.println(pre1.matches(initMessageTemplateStr)+" "+
          pre2.matches(initMessageTemplateStr)+" "+
          pre3.matches(initMessageTemplateStr)+" ");

      System.out.println(RandomStringUtils.randomAlphabetic(6).toLowerCase());
      stringSame();

	    String ccca ="aabbcc123";
	    System.out.println("----------"+ccca.matches(numZimu));

        String data ="28*30";
        int index = data.lastIndexOf("*");
        if(index>=0) {
            String rateStr =data.substring(index+1);
            System.out.println(data.substring(0,index));
            if(NumberUtils.isNumber(rateStr)) {
                System.out.println("haha"+rateStr);
            }
        }

	    String dataa="{\"head\":{\"os\":\" \",\"aaa\":\" \",\"ccc\":\" \",\"token\":\"aaaa\",\"yyy\":\" \"}}";
	    String aftera = dropInfo(dataa);
	    System.out.println(aftera);

	    System.out.println("aa".equalsIgnoreCase(null));
        String abce="1y94932749237";
        int ablen =abce.length();
        System.out.println(abce.substring(ablen-3));

	    String aa="${title}wwwaaabbb${aa}";
	    Pattern p = Pattern.compile(programTemplateStr);
        Matcher mt=p.matcher(aa);
        while(mt.find()) {
            System.out.println("---------------aac:" + mt.group(1));
        }

	    String abcc="ahiw_hsdihfi_sdhiihf";
	    String star="ahiw_";
	    System.out.println(abcc.indexOf("_",5));
        int len = star.length();
        int inx =abcc.indexOf("_",len);
        String pre=abcc.substring(len,inx);
        String nodeName =abcc.substring(inx+1);
        System.out.println(pre+"      "+nodeName);

        String nullStr=null;
        System.out.println("aa".equals(nullStr));
        System.out.println("Aa".equalsIgnoreCase("aA"));
        System.out.println("a,".split(",").length);

        System.out.println(Boolean.valueOf("false"));

        System.out.println("-------------------");
        System.out.println(Pattern.compile("\\s*[,]+\\s*").split("aa,,bb,cc")[1]);

        //System.out.println(String.valueOf(null));//报错
        System.out.println("aa".equals(null));//false;

        System.out.println("-------------------");

		boolean notDo=true;

        String keykey="2|_|1010|_|1|_|3|_|5";
        //keykey.intern();

        int index1= keykey.indexOf("|_|",  keykey.indexOf("|_|")+1);

        System.out.println(keykey.substring(index1));

        System.out.println("index1-->"+index1);

        System.out.println("......."+keykey.substring(keykey.length())+"////");

        int isYph= NumberUtils.toInt(null);

        System.out.println("isYph="+isYph);

        String cc=null;

        String abc ="";
        System.out.println("//////////////:"+abc.split(",").length);

        System.out.println("www"+cc);

        String key="C02421320";

        Matcher mm=pattern.matcher(key);

        if(mm.find()) {
            System.out.println(mm.group());
            System.out.println(mm.group(1));
        }
        checkIndex();

        if(notDo)
        {
            return;
        }

        System.out.println(String.format(pvSql, "223344","A-cc%"));
		
		System.out.println("123.22".indexOf("."));

        splitZw();

        //正则表达式验证
        reg();

        System.out.println(transCondition("5<="));
        System.out.println(transCondition("2<"));
        System.out.println(transCondition("3>"));
        System.out.println(transCondition("3.3>="));
        System.out.println(transCondition("2="));

        System.out.println(realyDataCondition(">=3.322"));

        List<String> strList = new ArrayList<String>();
        strList.add("a");
        strList.add("b");
        System.out.println(turn2InStr(strList));


        List<Integer> intList = new ArrayList<Integer>();
        intList.add(1);
        intList.add(2);
        System.out.println(turn2InStr(intList));


	}


    public static void reg()
    {
        Pattern p = Pattern.compile("[abcdefg]");

        //1<=a<2条件前部分reg字串
        String CDN_PRE_REG="[0-9\\.]{1,}(<=|<|>=|>|=)";

        //1<=a<2条件后部分reg字串
        String CDN_SUF_REG="(<=|<|>=|>|=)[0-9\\.]{1,}";

        String aa="k5<=a<20";

        Matcher m=p.matcher(aa);

        if(m.find()) {
            System.out.println(m.group());
            int index = m.start();
            String preStr = aa.substring(0, index);
            String afterStr = aa.substring(index + 1);

            System.out.println(preStr + " " + afterStr);

            Pattern p1 = Pattern.compile(CDN_PRE_REG);
            Matcher m1 = p1.matcher(preStr);

            if (m1.matches()) {
                System.out.println(m1.group());
            }

            Pattern p2 = Pattern.compile(CDN_SUF_REG);
            Matcher m2 = p2.matcher(afterStr);

            if (m2.matches()) {
                System.out.println(m2.group());
            }
        }

    }

	public static String turn2InStr(List<?> list)
    {

        String resultStr ="";
        if(list==null || list.size()<=0)
        {
            return resultStr;
        }
        StringBuffer resultBuf = new StringBuffer();
        for(Object obj:list)
        {
            if((obj instanceof Number) )
            {
                resultBuf.append(((Number)obj)).append(",");

            }else
            {
                resultBuf.append('\'').append(obj.toString()).append('\'').append(",");
            }

        }
        resultStr = resultBuf.toString();
        if(!StringUtils.isBlank(resultStr) && resultStr.endsWith(","))
        {
            resultStr=resultStr.substring(0,resultStr.length()-1);
        }
        return resultStr;
    }

    public static void splitZw()
    {
        String text ="23423，水电费hi,10%";
        String ts [] = text.split("，");
        System.out.println(ts.length);
        System.out.println(ts[0]+"   "+ts[1]);
    }

    /**
     * 从2<=a 变成a>=2
     */
    public static String transCondition(String preCdn)
    {
        if(preCdn.endsWith("<="))
        {
            String pre = preCdn.substring(0,preCdn.indexOf("<="));
            return ">="+pre;
        }
        if(preCdn.endsWith("<"))
        {
            String pre = preCdn.substring(0,preCdn.indexOf("<"));
            return ">"+pre;
        }

        if(preCdn.endsWith(">="))
        {
            String pre = preCdn.substring(0,preCdn.indexOf(">="));
            return "<="+pre;
        }

        if(preCdn.endsWith(">"))
        {
            String pre = preCdn.substring(0,preCdn.indexOf(">"));
            return "<"+pre;
        }

        if(preCdn.endsWith("="))
        {
            String pre = preCdn.substring(0,preCdn.indexOf("="));
            return "="+pre;
        }

        return preCdn;
    }

    public static String realyDataCondition(String targetValue)
    {
        String CDN_SUF_REG="(<=|<|>=|>|=)[0-9\\.]{1,}";
        Pattern p = Pattern.compile(CDN_SUF_REG);

         Matcher m =p.matcher(targetValue);
            if(m.find())
            {
                String pre = m.group(1);
                String dataStr = targetValue.replace(pre,"");
                    Double d = Double.parseDouble(dataStr);
                    d=d*100;
                    int value = d.intValue();
                    return pre+value;
            }
            return targetValue;

    }

    public static void checkIndex()
    {
        String path="/a/b/c/d/e";
        int len=path.length();
        int layerIndex = path.indexOf("/",1);
        String curPath=null;
        while(layerIndex>0 && layerIndex<(len-1))
        {
            curPath = path.substring(0,layerIndex);
            System.out.println(curPath+" "+layerIndex);
            layerIndex = path.indexOf("/",layerIndex+1);
        }
        if(!StringUtils.isBlank(curPath) && curPath.length()<len)
        {
            System.out.println(path);
        }
    }
}
