package com.shopstat.executor.extract;

import com.excel.ExcelUtil;
import com.shopstat.executor.IExecutor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class DataToExcelExecutor implements IExecutor {

    private Logger logger = LoggerFactory.getLogger(DataToExcelExecutor.class);

    private static String data_path ="e:\\ddd\\data.txt";

    private static String dir="e:\\ddd\\";

    private static final String [] STAT_FIELDS=new String[]{"NUMBER","KEY","顺序号"};

    private static final String [] STAT_KEYS=new String[] {"p","key","num"};

    public void exec(DateTime dateTime)
    {
        logger.info("DataToExcelExecutor exec begin,dateTime ="+dateTime);

        //读取数据
        Map<String,List<Integer>> data=readData();
        int count =data==null?0:data.size();
        logger.info("DataToExcelExecutor read data count="+count);

        //处理数据
        List<Map<String,Object>> list =dealData(data);
        int count1 = list==null?0:list.size();
        logger.info("DataToExcelExecutor result data count="+count1);

        //生成excel
        if(!new File(dir).exists()){
            new File(dir).mkdirs();
        }
        String fileName = dir+"data.xls";
        try {
            ExcelUtil.generate1(STAT_FIELDS, STAT_KEYS, list, fileName);
        }catch(Exception e)
        {
            logger.error("DataToExcelExecutor generate excel error,",e);
        }
        logger.info("DataToExcelExecutor exec end ..............");
    }

    //读取数据 数据每行格式 [key]\t[value]
    public Map<String,List<Integer>> readData()
    {
        Map<String,List<Integer>> data = new HashMap<String,List<Integer>>();
        BufferedReader reader =null;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(data_path)));
            String line=null;
            while((line = reader.readLine())!=null){
                if(!StringUtils.isBlank(line))
                {
                    String [] strs = line.split("\t");
                    if(strs.length==2)
                    {
                        String key = strs[0];
                        List<Integer> list = data.get(key);
                        if(list==null)
                        {
                            list = new ArrayList<Integer>();
                            data.put(key,list);
                        }
                        list.add(Integer.parseInt(strs[1]));
                    }
                }
            }
        }catch(Exception e){
            logger.error("DataToExcelExecutor readData read "+data_path+" exception",e);
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }catch(Exception e)
            {
                logger.error("DataToExcelExecutor reader close error,",e);
            }
        }
        return data;
    }

    public List<Map<String,Object>> dealData(Map<String,List<Integer>> data)
    {
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        for(String key:data.keySet())
        {
            List<Integer> per=data.get(key);
            if(per==null ||per.size()<=0)
            {
                continue;
            }
            int num=0;
            for(Integer p:per) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("p",p);
                map.put("key",key);
                map.put("num",num++);

                list.add(map);
            }
        }
        return list;
    }

}
