package com.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Pinyin4jUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(Pinyin4jUtil.class);
	
	private static Map<String, List<String>> pinyinMap = new HashMap<String, List<String>>(); 
	
	static {
		try {
			logger.info("Pinyin4jUtil static init duoyinzi_dic start.");
			initPinyin("duoyinzi_dic.txt");
		} catch (Exception e) {
			logger.error("Pinyin4jUtil fail to init duoyinzi_dic", e);
		}
		
	}
	
	public static String hanyu2Pinyin(String src) {
		char[] nameChar = src.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuilder pinyinBuffer = new StringBuilder();
		try {
			String[] pinyinArray = null;
			for (int i = 0, size = nameChar.length; i < size; i++) {
				if (nameChar[i] > 128) {
					pinyinArray = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
					if (pinyinArray != null && pinyinArray.length > 0) {
						pinyinBuffer.append(pinyinArray[0]);
					}
				} else {
					pinyinBuffer.append(nameChar[i]);
				}
			}
		} catch (Exception e) {
			logger.error("Pinyin4jUtil error,"+src, e);
		}
		return pinyinBuffer.toString();
    }
	
	public static String getPinYin(String chinese )
	{
		    StringBuffer pinyin = new StringBuffer(); 
		   
	        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat(); 
	        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE); 
	        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE); 
	        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	        try {
		        char[] arr = chinese.toCharArray(); 
		   
		        for (int i = 0; i < arr.length; i++) { 
		   
		            char ch = arr[i]; 
		   
		            if (ch > 128) { // 非ASCII码 
		                // 取得当前汉字的所有全拼 
		                try { 
		   
		                    String[] results = PinyinHelper.toHanyuPinyinStringArray( ch, defaultFormat); 
		   
		                    if (results == null || results.length<=0) {  //非中文 
		   
		                        //return ""; 
		                    } else { 
		   
		                        int len = results.length; 
		   
		                        if (len == 1) { // 不是多音字 
		   
		                             pinyin.append(results[0]);
		                               
		                        }else if(results[0].equals(results[1])){    //非多音字 有多个音，取第一个 
		                               
		                            pinyin.append(results[0]);
		                            
		                        }else { // 多音字 
		                               
		                            //System.out.println("多音字："+ch); 
		                               
		                            int length = chinese.length(); 
		                               
		                            boolean flag = false; 
		                               
		                            String s = null; 
		                               
		                            List<String> keyList =null; 
		                               
		                            for (int x = 0; x < len; x++) { 
		                                   
		                                String py = results[x]; 
		               
		                                keyList = pinyinMap.get(py); 
		                                   
		                                if (i + 3 <= length) {   //后向匹配2个汉字  大西洋  
		                                    s = chinese.substring(i, i + 3); 
		                                    if (keyList != null && (keyList.contains(s))) { 
		                                  
		                                        pinyin.append(results[x]); 
		                                        flag = true; 
		                                        break; 
		                                    } 
		                                } 
		                                   
		                                if (i + 2 <= length) {   //后向匹配 1个汉字  大西 
		                                    s = chinese.substring(i, i + 2); 
		                                    if (keyList != null && (keyList.contains(s))) { 
		   
		                                        
		                                        pinyin.append(results[x]); 
		                                        flag = true; 
		                                        break; 
		                                    } 
		                                } 
		                                   
		                                if ((i - 2 >= 0) && (i+1<=length)) {  // 前向匹配2个汉字 龙固大 
		                                    s = chinese.substring(i - 2, i+1); 
		                                    if (keyList != null && (keyList.contains(s))) { 
		                                        pinyin.append(results[x]); 
		                                        flag = true; 
		                                        break; 
		                                    } 
		                                } 
		                                   
		                                if ((i - 1 >= 0) && (i+1<=length)) {  // 前向匹配1个汉字   固大 
		                                    s = chinese.substring(i - 1, i+1); 
		                                    if (keyList != null && (keyList.contains(s))) { 
		                                       
		                                        pinyin.append(results[x]); 
		                                        flag = true; 
		                                        break; 
		                                    } 
		                                } 
		                                   
		                                if ((i - 1 >= 0) && (i+2<=length)) {  //前向1个，后向1个      固大西 
		                                    s = chinese.substring(i - 1, i+2); 
		                                    if (keyList != null && (keyList.contains(s))) { 
		                                         
		                                        pinyin.append(results[x]); 
		                                       
		                                        flag = true; 
		                                        break; 
		                                    } 
		                                } 
		                            } 
		                               
		                            if (!flag) {    //都没有找到，匹配默认的 读音  大  
		                                   
		                                s = String.valueOf(ch); 
		                                   
		                                for (int x = 0; x < len; x++) { 
		                                       
		                                    String py = results[x]; 
		                                       
		                                   
		                                    keyList = pinyinMap.get(py); 
		                                       
		                                    if (keyList != null && (keyList.contains(s))) { 
		                                        
		                                      pinyin.append(results[x]); 
		                                        
		                                        break; 
		                                    } 
		                                } 
		                            } 
		                        } 
		                    } 
		   
		                } catch (BadHanyuPinyinOutputFormatCombination e) { 
		                    e.printStackTrace(); 
		                } 
		            } else { 
		                pinyin.append(arr[i]); 
		            } 
		        }
	        } catch (Exception e) {
	        	logger.error("Pinyin4jUtil error,"+chinese, e);
			}
		    return pinyin.toString(); 
	}
	
	/**
     * 初始化 所有的多音字词组
     * 
     * @param fileName
     */ 
    public static void initPinyin(String fileName) { 
        // 读取多音字的全部拼音表; 
        InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(file)); 
   
        String s = null; 
        try { 
            while ((s = br.readLine()) != null) { 
   
                if (s != null) { 
                    String[] arr = s.split("#"); 
                    String pinyin = arr[0]; 
                    String chinese = arr[1]; 
   
                    if(chinese!=null){ 
                        String[] strs = chinese.split(" "); 
                        List<String> list = Arrays.asList(strs); 
                        pinyinMap.put(pinyin, list); 
                    } 
                } 
            } 
   
        } catch (IOException e) { 
            e.printStackTrace(); 
        }finally{ 
            try { 
                br.close(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
    } 
	
	public static void main(String args [])
	{
		  String cnStr = "长沙";
		  String cnStr2="重庆";
	      System.out.println(hanyu2Pinyin(cnStr)); 
	      System.out.println(getPinYin(cnStr)); 
	      
	      System.out.println(hanyu2Pinyin(cnStr2));
	      System.out.println(getPinYin(cnStr2));
	}
}
