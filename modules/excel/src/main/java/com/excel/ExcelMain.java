package com.excel;

import com.model.SiteAccessInfo;
import com.util.DataTransUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel根据模板文件生成带数据的excel文件
 * @author cheng
 *
 */
public class ExcelMain {

    public static void main(String args[]) throws Exception
    {
    	Map<String,Object> map = new HashMap<String,Object>();
        List<SiteAccessInfo> list = generalTestData();
        map.put("data",list);
        //根据excel模板生成excel文件
        makeExcel(map);

        //直接生成excel文件
        String titles [] ={"站点名","url","uv","out","pc曝光","无线曝光"};
        String keys [] ={"name","site","uv","out","pcExp","wxExp"};
        List<Map> values = DataTransUtil.listBean2ListMap(list);

        ExcelUtil.generateXls(titles,keys,values,"e:/tmp/cc.xls");
    }

    public static void makeExcel( Map<String, Object> map)
    {
        String dir = "e:/tmp/";
        OutputStream os=null;
        try {
            if(!new File(dir).exists()){
                Boolean createFile = new File(dir).mkdirs();
                if(createFile){
                    System.out.println("创建目录成功");
                }else{
                	 System.out.println("创建目录失败");
                }
            }
  
            os = new FileOutputStream(dir+"out.xls"); // 取得输出流
            if(map==null)
            {
            	 System.out.println("data is null.");
                 map = new HashMap<String, Object>();
            }
            ExcelUtil.buildExcelByTemplate("excelTemplate.xls",map, os);
        } catch (Exception e) {
        	System.out.println("makeExcel error.");
        	e.printStackTrace(System.out);
        }finally {
            if(os!=null)
            {
                try {
                    os.close();
                }catch(Exception ee)
                {
                    ee.printStackTrace(System.out);
                }
            }
        }
    }

    public static  List<SiteAccessInfo> generalTestData()
    {
        List<SiteAccessInfo> list = new ArrayList<SiteAccessInfo>();
        SiteAccessInfo info = new SiteAccessInfo();
        info.setSite("http://www.baidu.com");
        info.setName("百度");
        info.setUv(10000*10000l);
        info.setOut(10000*1000l);
        info.setPcExp(10000*9000l);
        info.setWxExp(10000*2000l);
        list.add(info);

        SiteAccessInfo info2 = new SiteAccessInfo();
        info2.setSite("http://www.yy.com");
        info2.setName("YY");
        info2.setUv(1000*5000l);
        info2.setOut(1000*3000l);
        info2.setPcExp(1000*700l);
        info2.setWxExp(1000*2000l);
        list.add(info2);

        return list;
    }

}
