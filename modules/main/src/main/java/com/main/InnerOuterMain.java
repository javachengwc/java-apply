package com.main;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.entity.Rs;
import com.util.HexUtil;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


public class InnerOuterMain {
	
	public static final Pattern urlParamerPattern = Pattern.compile("q-(\\d+)-(\\d+)-(\\d+)-(\\d+)-(\\d+)-(\\d+)-(\\d+)-([a-zA-Z_]+\\d+-)?([^-]*)-(\\d+)");
	
	public static final Pattern transOldUrlPattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)-([a-zA-Z_]+\\d+-)?"+"sort_");
	
	private static final Pattern urlPagePattern= Pattern.compile("/(\\d+)\\.html");
	
	private static final Pattern urlParamerPattern1 = Pattern.compile("(name|city|list)-(\\d+)");
	
	public static Pattern domainPtn =Pattern.compile("^(http://)?(www\\.)?([^\\.])*\\.(.)*/(\\d+)\\.html");
	
	public static Pattern domainParamPtn =Pattern.compile("/(\\d+)\\.html");
	
	public static Pattern specParamPtn = Pattern.compile("((.+)->(,)?)+");
	
	
    public static void main(String[] args)   throws Exception
    {   
    	
    	int endFlag=1;
    	
		
		if(endFlag==1)
		{
			return;
		}
    	
//    	encode();
    	
    	String oldString="select * from (select * from bb) | SLEEP (2*100) |ALERT table ba | show tables | show database ";
    	String value = oldString.replaceAll("((?i)SELECT.*FROM|UPDATE.*SET|DELETE.*FROM|UNION.*SELECT|SLEEP\\s*\\()", "");
        value = value.replaceAll("((?i)DROP\\s*TABLE|DROP\\s*DATABASE|CREATE\\s*TABLE|CREATE\\s*DATABASE|TRUNCATE|ALERT\\s*TABLE)", "");
        value = value.replaceAll("((?i)ALERT\\s*DATABASE|SHOW\\s*TABLE|SHOW\\s*DATABASE|INSERT.*INTO|REPLACE.*INTO)", "");
        
        System.out.println(value);
    	
    	System.out.println("goha"+StringUtils.isBlank(" "));
    	
    	String nameba="random"+new Random().nextInt(100000);
    	System.out.println(nameba);
    	
    	String urlString = "http://m.meilele.com/special_wechat/1.html";
    	Matcher murl = domainPtn.matcher(urlString);
    	String ttaabb ="";
		if(murl.find())
		{
			ttaabb = murl.group();
			String referDomainUrl =murl.group(5);
			System.out.println("gaga  refermatch ="+referDomainUrl);
			System.out.println(ttaabb+" "+urlString.indexOf(referDomainUrl+".html"));
		}else
		{
			System.out.println("result is not find");
		}
    	
    	System.out.println("A".equalsIgnoreCase("a"));
    	
    	System.out.println("Integer:"+Integer.class.getClassLoader());
    	System.out.println("StringUtils:"+StringUtils.class.getClassLoader());
    	
    	Integer aab=200;
    	Integer bba=200;
    	
    	System.out.println(aab==bba.intValue());
    	
    	Pattern ptnA = Pattern.compile("/^/category-chuang//");
		Matcher match = ptnA.matcher("/category-chuang/haha.html");
		if(!match.find())
		{
			System.out.println("not match");
		}else
		{
			System.out.println("match"+match.group(0));
		}
		
    	
    	List<String> listA = new ArrayList<String>();
    	List<String> listB = new ArrayList<String>();
    	String abc= new String("abc");
    	listB.add(abc);
    	listA.addAll(listB);
        listB.clear();
        System.out.println(listA.size()+" "+listB.size());
    	
    	
    	
    	Calendar ca= Calendar.getInstance();
    	System.out.println(ca.get(Calendar.DAY_OF_WEEK));
    	
    	String abck="a-*/2";
    	System.out.println(abck.split("-").length+" "+abck.split("\\/")[0] );
    	
    	String ttaa="我们的祖国叫什么？||中国|China|中华人民共和国";
    	
    	String tmpss []= ttaa.split("\\|\\|");
    	System.out.println(tmpss.length+" "+ tmpss[1]);
    	
       
    	
    	System.out.println(needLoginBackstage(2));
    	
    	System.out.println("current:"+System.currentTimeMillis());
    	
    	String stra="���������������";
    	System.out.println(new String(stra.getBytes("gbk"),"utf8"));
    	
    	String url="www.hhh.com/zhuangshi/list-1/23.html";
		Matcher tmpMm= urlPagePattern.matcher(url);
    	if(tmpMm.find())
    	{
    	  System.out.println("ouya "+tmpMm.group()+" "+tmpMm.group(1));
    	}
        tmpMm= urlParamerPattern1.matcher(url);
        if(tmpMm.find())
    	{
    	      System.out.println("ouya "+tmpMm.group()+" "+tmpMm.group(1)+" "+ tmpMm.group(2));
    	}
    	
    	System.out.println(System.getenv("JAVA_HOME"));
    	System.out.println("a");
    	System.out.println(System.getProperty("JAVA_HOME"));
    	
    	System.out.println(getRadioIndex(5)+" yaya");
    	String tmpUrl ="/rwer/q-0-0-0-0-0-0-0-sort_most1-1/";
    	String tmpUrl2= "/rwer/q-0-0-0-0-0-0-0-full1-sort_most1-1/";
    	Matcher tmpM= transOldUrlPattern.matcher(tmpUrl);
    	if(tmpM.find())
    	{
    		int a = tmpM.groupCount();
    		System.out.println("a:"+a+" "+ tmpM.group());
    		for(int i=1;i<=a;i++)
    		{
    			String key =tmpM.group(i);
    			if(key==null)
    			{
    				System.out.println("k is null");
    			}
    		     System.out.println(key);
    		}
    	}
    	
    	String aaddd="jah%s\r\nbbb%s";
    	System.out.println(String.format(aaddd, "gogog","aaa"));
    	System.out.println(aaddd);
    	Pattern ptn = Pattern.compile("aaabb");
    	Matcher mmm = ptn.matcher("aaabb");
    	if(mmm.find())
    	{
    		System.out.println("you a");
    	}
    	if(mmm.find())
    	{
    		System.out.println("ne a");
    		
    	}else
    	{
    		System.out.println("ne abc");
    	}
    	
    	System.out.println("".length());
    	boolean only=true;
    	String aabb=",100";
    	String pt="\\D100\\D|\\D100$|^100\\D|^100$";
    	//String p="100";
    	Pattern pp = Pattern.compile(pt);
    	Matcher m=pp.matcher(aabb);
    			
    	System.out.println("find:"+m.find()+" " );
    	
    	String ppp="a,b,v|d,ef";
    	System.out.println(ppp.replace("|",";"));
    	
    	if(only)
    	{
    	  return;
    	}
    	
    	
    	String tta="a,b,c;d,e,f";
    	String kk = tta.split(",")[0];
    	System.out.println(kk);
    	
    	System.out.println(""+null);
    	System.out.println((""+2).equalsIgnoreCase("2"));
    	System.out.println(("").equalsIgnoreCase(""));
    	
    	String  dateStr="2014.9.9";
        System.out.println(getMissFromDateStr(dateStr));
        
        String pubDateStr="2014-08-11 00:00:00";
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date upbDateBegin=format.parse(pubDateStr);
    	long upBegin = upbDateBegin.getTime();
    	long upEnd =upBegin +1*24*60*60*1000; 
    	if(System.currentTimeMillis()>=upBegin && System.currentTimeMillis()<upEnd)
    	{
    		System.out.println("hahah");
    	}
    	System.out.println("hahah");
    	System.out.println("aaa".equalsIgnoreCase(null));
    	String code="Ab";
    	String captcha="aBa";
    	System.out.println(code.equalsIgnoreCase(captcha));
    	String text ="cc22美丽的sdfisdh-aa";
    	for(char str:text.toCharArray())
    	{
    		System.out.println(str+" "+isChinese(str));
    	}
    	System.out.println(onlyChineseStr(text));
    	
    	System.out.println(UUID.randomUUID().toString());
    	//System.out.println(DigestUtils.md5Hex("中文"));
    	
    	List<String> ll  =new ArrayList<String>();

    	ll.add("ba");
    	ll.add("ab");
    	
    	Collections.sort(ll,  new Comparator<String>(){
    		
    		 public int compare(String v1, String v2)
			 {
    			 char a = v1.toCharArray()[0];
    			 char b = v2.toCharArray()[0];
    			 if(a<b)
    			 {
    				 return 1;
    			 }
    			 if(a>b)
    			 {
    				 return -1;
    			 }
    			 return 0;
			 }
    	});
    	
    	for(String s:ll)
    	{
    		System.out.println(s);
    	}

     	  DateFormat formataaaa = new SimpleDateFormat("yyyy年MM月dd日");
 		   String endTimeStr =formataaaa.format(new Date());
 		   System.out.println("endTimeStr:"+endTimeStr);
     	//System.out.println(":"+stringConvertor(null));
     	JSONObject jsonObject = new JSONObject();
 		jsonObject.put("errcode",2);
 		jsonObject.put("errmsg", "多次发送都失败");
     	
     	System.out.println("jsonObject.errcode="+jsonObject.getString("errcode"));
     	
 		
     	String aa ="2015-01-21 13:30:00";
     	DateFormat formata = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     	Date ddat= formata.parse(aa);
     	
     	String baDay ="2015-01-21 13:51:00";
     	Date baDayd= formata.parse(baDay);
     	
     	System.out.println("chaju:"+getTodayNum(baDayd,ddat));
     	
     	
     	
     	System.out.println(System.getProperties());
     	System.out.println(Charset.defaultCharset());
     	
     	Rs rss = new Rs();
     	rss.setId(2);
     	rss.setName("aa");
     	
     	System.out.println(JSON.toJSONString(rss));

    }
    
    /**
	 * 排除非中文
	 */
	public static String onlyChineseStr(String str)
	{
		if(StringUtils.isBlank(str))
		{
			return str;
		}
		StringBuffer buf = new StringBuffer("");
		for(char ch:str.toCharArray())
		{
			if(isChinese(ch))
			{
				buf.append(ch);
			}
		}
		return buf.toString();
	}
    
    public static final boolean isChinese(char str) 
	{
		if ((str >= 0x4e00) && (str <= 0x9fbb)) 
		{
			return true;
		}
		return false;
	}
    
    public static final Pattern chinesPattern=Pattern.compile("[\\w|-]*");
    
    public static Pattern smsTmpContentPattern  =Pattern.compile("\\{\\$([\\w\\W]*?)\\}"); //短信模板模式
	
	public static Pattern smsTmpComtentSpecPattern = Pattern.compile("\\[([\\w\\W]*?)\\]");//特殊的短信模板模式
   
	public static Map<String,String> getDataKey(String express)
	{
		Map<String,String> map = new HashMap<String,String>();
		Matcher m = smsTmpContentPattern.matcher(express);
		while(m.find())
		{
			map.put(m.group(1),m.group());
		}
		if(map.size()==0)
		{
			Matcher m1 = smsTmpComtentSpecPattern.matcher(express);
			while(m1.find())
			{
				map.put(m1.group(1),m1.group());
			}
		}
		return map;
	}
	
    
    private static final String urlRegex = "(\\d{1,}-){1,5}(\\d{1,})";
	private static final Pattern urlParamerPattern2=Pattern.compile(urlRegex);
	private static final String urlNoParamRegex = "/category";
	private static final Pattern urlNoParamPattern =Pattern.compile(urlNoParamRegex);
    
	public static String transformSolrMetacharactor(String input){
        StringBuffer sb = new StringBuffer();
        String regex = "[+/\\-&|!(){}\\[\\]^\"~*?:(\\)<>]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()){
            matcher.appendReplacement(sb, "\\\\\\\\"+matcher.group());
        }
        matcher.appendTail(sb);
        return sb.toString(); 
    }
	
	public static String toHexString(String s)
	{
		String str="";
		for (int i=0;i<s.length();i++)
		{
		int ch = (int)s.charAt(i);
		String s4 = Integer.toHexString(ch);
		str = str + s4;
		}
		return str;
	} 
	
	/**
	 * 获取合适的实拍图
	 * @return
	 */
	public static String getSuitableImg(String figurePhotos,String img2)
	{
		String imgVu="";
		
		if(StringUtils.isBlank(figurePhotos))
		{
			return imgVu;
		}
		String imgs [] = figurePhotos.split(",");
		int len = imgs.length;
		System.out.println(len);
		for(int i=len-1;i>=0;i--)
		{
			String im =imgs[i];
			System.out.println(im);
			if(StringUtils.isBlank(im) || StringUtils.isBlank(im.trim()))
			{
				continue;
			}
			if(StringUtils.isBlank(img2) || !im.trim().equalsIgnoreCase(img2))
			{
				imgVu= im.trim();
				break;
			}
		}
		return imgVu;
	}
	
	 public static String generationValidateCode(int randomCaptchat) {
			String captchat=String.valueOf(randomCaptchat);
			Long result=1l;
			for(int i=0;i<captchat.length();i++){
				if(Long.parseLong(Character.toString(captchat.charAt(i)))!=0L)
					System.out.println(Long.parseLong(Character.toString(captchat.charAt(i))));
				   result=result*Long.parseLong(Character.toString(captchat.charAt(i)));
			}
			result=result*randomCaptchat;
			return String.valueOf(result);
	}
	 
	 @SuppressWarnings("unchecked")
	public static String getAppPath(Class cls) {
		// 检查用户传入的参数是否为空
		if (cls == null)
			throw new java.lang.IllegalArgumentException("参数不能为空！");
		ClassLoader loader = cls.getClassLoader();
		// 获得类的全名，包括包名
		String clsName = cls.getName() + ".class";
		// 获得传入参数所在的包
		Package pack = cls.getPackage();
		String path = "";
		// 如果不是匿名包，将包名转化为路径
		if (pack != null) {
			String packName = pack.getName();
			// 此处简单判定是否是Java基础类库，防止用户传入JDK内置的类库
			if (packName.startsWith("java.") || packName.startsWith("javax."))
				throw new java.lang.IllegalArgumentException("不要传送系统类！");
			// 在类的名称中，去掉包名的部分，获得类的文件名
			clsName = clsName.substring(packName.length() + 1);
			// 判定包名是否是简单包名，如果是，则直接将包名转换为路径，
			if (packName.indexOf(".") < 0)
				path = packName + "/";
			else {// 否则按照包名的组成部分，将包名转换为路径
				int start = 0, end = 0;
				end = packName.indexOf(".");
				while (end != -1) {
					path = path + packName.substring(start, end) + "/";
					start = end + 1;
					end = packName.indexOf(".", start);
				}
				path = path + packName.substring(start) + "/";
			}
		}
		// 调用ClassLoader的getResource方法，传入包含路径信息的类文件名
		java.net.URL url = loader.getResource(path + clsName);
		// 从URL对象中获取路径信息
		String realPath = url.getPath();
		// 去掉路径信息中的协议名"file:"
		int pos = realPath.indexOf("file:");
		if (pos > -1)
			realPath = realPath.substring(pos + 5);
		// 去掉路径信息最后包含类文件信息的部分，得到类所在的路径
		pos = realPath.indexOf(path + clsName);
		realPath = realPath.substring(0, pos - 1);
		// 如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
		if (realPath.endsWith("!"))
			realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		/*------------------------------------------------------------ 
		 ClassLoader的getResource方法使用了utf-8对路径信息进行了编码，当路径 
		  中存在中文和空格时，他会对这些字符进行转换，这样，得到的往往不是我们想要 
		  的真实路径，在此，调用了URLDecoder的decode方法进行解码，以便得到原始的 
		  中文及空格路径 
		-------------------------------------------------------------*/
		try {
			realPath = java.net.URLDecoder.decode(realPath, "utf-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println("realPath----->"+realPath);
		return realPath;
	}
	 
	public static String transExpress(String express)
	{
		int d = express.lastIndexOf(" ");
		String lastStr = express.substring(d+1);
		lastStr = lastStr.replace("0", "7");//新期天的转换 
		String rt = "0 "+express.substring(0,d+1)+lastStr;
		return rt;
	}
	
	public static Long getMissFromDateStr(String dateStr)
	{
		if(StringUtils.isBlank(dateStr))
		{
			return 0l;
		}
		/////
		DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
		try{
		    Date d = format.parse(dateStr);
		    return d.getTime();
		}catch(Exception e)
		{
			return null;
		}
	}
	
	public static int getRadioIndex(int value)
	{
		
		
		int index=-1;
		while(value>0)
		{   
			index++;
			if(value%2>0)
			{
				break;
			}
			value= value/2;
		}
		return index;
	}
	
	public static void encode() { 
        String name = "I am 君山"; 

        System.out.println("----def-------");
        HexUtil.printBytes(name.getBytes());
        try { 

            System.out.println("-----ISO-8859-1-------");
            byte[] iso8859 = name.getBytes("ISO-8859-1");
            HexUtil.printBytes(iso8859);

            System.out.println("-----GB2312-------");
            byte[] gb2312 = name.getBytes("GB2312");
            HexUtil.printBytes(gb2312);
            System.out.println("------GBK------");
            byte[] gbk = name.getBytes("GBK");
            HexUtil.printBytes(gbk);
            System.out.println("------UTF-16------");
            byte[] utf16 = name.getBytes("UTF-16");
            HexUtil.printBytes(utf16);
            System.out.println("------UTF-8------");
            byte[] utf8 = name.getBytes("UTF-8");
            HexUtil.printBytes(utf8);
            System.out.println("------------");
        } catch (UnsupportedEncodingException e) { 
            e.printStackTrace(); 
        } 
    }
	
	public static String stringConvertor(Object obj) {
		if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof Integer) {
			
			return String.valueOf(obj);
		} else {
			return "";
		}
	}
	
	public static int getTodayNum(Date nowDate,Date begin)
	{
		long now = nowDate.getTime();
		
		//还没开始
		if(begin.getTime()> now )
		{
			return 0;
		}	
		
		Calendar ca = Calendar.getInstance();
		ca.setTime(begin);
		
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND,0);
		ca.set(Calendar.MILLISECOND,0);
		
		long beginMill = ca.getTimeInMillis();

		long dayMill = 1*24*60*60*1000l;
		long count = (now-beginMill)/dayMill;
		
		return new Long(count).intValue()+1;
		
	}
	
	public static  boolean needLoginBackstage(int loginFlag)
	{
		if((loginFlag & 2)==2)
		{
			return true;
		}
		return false;
	}
	
	
}
