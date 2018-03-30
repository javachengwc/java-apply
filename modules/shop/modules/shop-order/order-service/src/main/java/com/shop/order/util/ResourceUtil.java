package com.shop.order.util;

import com.util.base.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ResourceUtil {

    private static Logger logger= LoggerFactory.getLogger(ResourceUtil.class);

    //获取表达式目录下的各文件内容，
    //表达式目录可以是文件目录，classpath目录，jar包中的目录
    //比如patternPath=aa/*.txt,就是获取aa目录下所有txt文件内容
    public static Map<String,String> loadPatternResourceData(String patternPath) {
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver= new PathMatchingResourcePatternResolver();
        logger.info("ResourceUtil loadPatternResourceData start......................");
        try {
            Resource[] rss = pathMatchingResourcePatternResolver.getResources(patternPath);
            int len = rss == null ? 0 : rss.length;
            logger.info("ResourceUtil loadPatternResourceData file count={}",len);
            if(len<=0) {
                return Collections.EMPTY_MAP;
            }
            Map<String,String> dataMap = new HashMap<String,String>();
            for(Resource rs:rss) {
                String fileName =rs.getFilename();
                String fileData =loadResourceData(rs);
                dataMap.put(fileName,fileData);
            }
            return dataMap;
        }catch(Exception e) {
            logger.error("ResourceUtil loadPatternResourceData error,",e);
        }
        return Collections.EMPTY_MAP;
    }

    public static  String loadResourceData(Resource rs) {
        String fileName =rs.getFilename();
        String data = null;
        InputStream in=null;
        logger.info("ResourceUtil loadResourceData start,resource fileName={},",fileName);
        try {
            in =rs.getInputStream();
            data = StreamUtil.inputStream2String(in, "UTF-8");
        } catch (Exception e) {
            logger.error("ResourceUtil loadResourceData error,resource fileName={},",fileName, e);
        }finally {
            if(in!=null)
            {
                try{
                    in.close();
                }catch(Exception ee)
                {
                    logger.error("ResourceUtil loadResourceData close inputStream error," , ee);
                }
            }
        }
        return data;
    }
}
