package com.hadoop.service;

import com.hadoop.model.UserDailyStatBvo;
import com.hadoop.task.base.HadoopBaseTask;
import com.util.col.MapUtil;
import com.util.base.NumberUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 用户每天数据服务类
 * 数据量大处理不了，只是一个写入hdfs文件的一个例子
 */
@Service
public class UserDailyStatService extends HadoopBaseTask {


    private static final Logger logger = LoggerFactory.getLogger(UserDailyStatService.class);

    private static final String separator = "\001";
    private static final String srcPath = "/tmp/";
    private static final String winSrcPath="e:/";
    private static final String dstPath = "/input/hive/userdailystat/";

    public static enum QueryEnum{

        PV("pv"),OUT("out"),ORDER("订单"),SIGN("签到");

        private QueryEnum(String note)
        {
            this.note=note;
        }

        private String note;

        public String getNote()
        {
            return this.note;
        }

        public void setNote(String note)
        {
            this.note=note;
        }
    }

    //查pv
    private static String pvSql="select partitiontime,user,user_id,count(1) as pv \n" +
            " from nginxlog \n" +
            " where partitiontime='%s' \n" +
            " and (user!='' or user_id !='') \n" +
            " group by partitiontime,user,user_id limit 5000";
    //%s  ------20150707

    //查out
    private static String outSql="select partitiontime,user,user_id,count(1) as out \n" +
            " from outlog \n" +
            " where partitiontime='%s' \n" +
            " and (user!='' or user_id !='') \n" +
            " group by partitiontime,user,user_id limit 5000";;

    //%s  ------20150707

    //查订单
    private static String orderSql="select partitiontime,user,userid as user_id,count(1) as orderCount,sum(amount) as orderAmount \n" +
            " from order \n" +
            " where partitiontime='%s' \n" +
            " and userid!=''\n" +
            " group by partitiontime,user,userid limit 5000" ;

    //%s  ------20150707;

    //查签到
    private static String signSql ="select partitiontime,user_id,count(1) as signCount \n" +
            " from scores \n" +
            " where partitiontime='%s' \n" +
            " group by partitiontime,user_id limit 5000";

    //%s  ------20150707

    private JdbcTemplate jdbcTemplate;


    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void handle(DateTime dateTime) {
        boolean initSucc = initHadoopClient();
        if (!initSucc) {
            logger.error("初始化hadoop客户端失败!");
            return;
        }
        logger.info("初始化hadoop客户端成功!");

        //准备数据
        List<UserDailyStatBvo> list = new ArrayList<UserDailyStatBvo>();

        logger.info("----------------------UserDailyStatService exec queryData begin---------------------");
        List<Map<String,Object>> pvData =queryPv(dateTime);
        int pvSize = ((pvData==null)?0:pvData.size());
        logger.info("----------------------UserDailyStatService exec queryPv size="+pvSize+"---------------------");
        merginData(list,pvData,QueryEnum.PV);
        pvData.clear();
        logger.info("----------------------UserDailyStatService merginData pvData end---------------------");


        List<Map<String,Object>> outData =queryOut(dateTime);
        int outSize = ((outData==null)?0:outData.size());
        logger.info("----------------------UserDailyStatService exec queryOut size="+outSize+"---------------------");
        merginData(list,outData,QueryEnum.OUT);
        outData.clear();
        logger.info("----------------------UserDailyStatService merginData outData end---------------------");

        List<Map<String,Object>> orderData =queryOrder(dateTime);
        int orderSize = ((orderData==null)?0:orderData.size());
        logger.info("----------------------UserDailyStatService exec queryOrder size="+orderSize+"---------------------");
        merginData(list, orderData, QueryEnum.ORDER);
        orderData.clear();
        logger.info("----------------------UserDailyStatService merginData orderData end---------------------");

        List<Map<String,Object>> signData =querySign(dateTime);
        int signSize = ((signData==null)?0:signData.size());
        logger.info("----------------------UserDailyStatService exec querySign size="+signSize+"---------------------");
        merginData(list, signData, QueryEnum.SIGN);
        signData.clear();
        logger.info("----------------------UserDailyStatService merginData signData end---------------------");

        logger.info("----------------------UserDailyStatService exec queryData end---------------------");


        int dataSize=list.size();
        logger.info("----------------------UserDailyStatService write data size="+dataSize+"--------------------");

        for(int i=0;i<10 && i<dataSize ;i++)
        {
            logger.info("----------simple data:"+list.get(i));
        }

        writeDataToTxt(dateTime, list,fileSystem);

    }


    public void merginData(List<UserDailyStatBvo> list , List<Map<String,Object>> data,QueryEnum queryEnum)
    {
        Map<String,String> pkMap = new HashMap<String,String>();
        pkMap.put("user","utma");
        pkMap.put("user_id","userId");

        Map<String,String> colMap=new HashMap<String,String>();

        switch (queryEnum)
        {
            case PV:
                if(list.size()<=0)
                {
                    firstMerginPv(list,data);
                }else {
                    colMap.clear();
                    colMap.put("pv", "pv");
                    merginData(list, data, pkMap, colMap);
                }
                break;
            case OUT:
                colMap.clear();
                colMap.put("out","out");
                merginData(list,data,pkMap,colMap);
                break;
            case ORDER:
                pkMap.clear();
                pkMap.put("user_id","userId");
                colMap.clear();
                colMap.put("orderCount","orderCount");
                colMap.put("orderAmount","orderAmount");
                merginData(list,data,pkMap,colMap);
                break;
            case SIGN:
                pkMap.clear();
                pkMap.put("user_id","userId");
                colMap.clear();
                colMap.put("signCount","signCount");
                break;
        }
    }

    /**
     * 第一次合并pv数据
     * @param list
     * @param data
     */
    public void firstMerginPv(List<UserDailyStatBvo> list ,List<Map<String,Object>> data)
    {
        if(data==null)
        {
            return;
        }
        for(Map<String,Object> map:data)
        {
            UserDailyStatBvo stat = new UserDailyStatBvo();

            Object utmaObj = map.get("user");
            Object userIdObj =map.get("user_id");
            Object pvObj = map.get("pv");

            if(utmaObj!=null)
            {
                stat.setUtma(utmaObj.toString());
            }
            if(userIdObj!=null)
            {
                stat.setUserId(userIdObj.toString());
            }
            if(pvObj!=null && NumberUtil.isNumeric(pvObj.toString()))
            {
                stat.setPv(Integer.parseInt(pvObj.toString()));

            }
            list.add(stat);
        }
    }

    /**
     * 合并数据
     * @param list
     * @param data
     * @param pkMap
     * @param colMap
     */
    public void merginData(List<UserDailyStatBvo> list ,List<Map<String,Object>> data,Map<String,String> pkMap, Map<String,String> colMap)
    {
        if(list==null || (pkMap==null || pkMap.size()<=0) || (colMap==null || colMap.size()<=0))
        {
            return;
        }

        List<Map<String,Object>> tmpList=new ArrayList<Map<String,Object>>();
        int i=0;
        for(Map<String,Object> map :data)
        {
            i++;
            logger.info("------------mergin:"+i+"----------------");
            boolean find =false;

            for(UserDailyStatBvo bvo:list)
            {

                if(!isSuit(bvo,map,pkMap))
                {
                    continue;
                }

                find=true;
                Map<String,Object> mapValue =new HashMap<String,Object>();
                for(String key:colMap.keySet())
                {

                    // logger.info("-----key:"+key+", anKey:"+colMap.get(key)+",value:"+map.get(key));
                    mapValue.put(colMap.get(key), map.get(key));
                }
                MapUtil.injectValue(bvo, mapValue);
            }

            if(!find)
            {
                tmpList.add(map);

            }

        }
        logger.info("------------needAdd size:"+tmpList.size()+"----------------");
        if(tmpList.size()>0)
        {

            for(Map<String,Object> map :tmpList)
            {
                UserDailyStatBvo stat = new UserDailyStatBvo();

                Map<String, Object> mapValue = new HashMap<String, Object>();
                for (String key : pkMap.keySet()) {
                    mapValue.put(colMap.get(key), map.get(key));
                }

                for (String key : colMap.keySet()) {
                    mapValue.put(colMap.get(key), map.get(key));
                }
                MapUtil.injectValue(stat, mapValue);

                list.add(stat);
            }
        }
        tmpList.clear();
    }

    /**
     * 是否匹配
     */
    public boolean isSuit(UserDailyStatBvo bvo,Map<String,Object> map,Map<String,String> pkMap)
    {

        Map<String,Object> key=new HashMap<String,Object>();
        Map<String,Object> key1 = new HashMap<String,Object>();

        for(String perKey:pkMap.keySet())
        {
            Object obj = map.get(perKey);
            key.put(perKey,obj);
            Object obj1 =null;
            try{
                obj1= BeanUtils.getProperty(bvo, pkMap.get(perKey));
            }catch(Exception e)
            {

            }
            key1.put(perKey,obj1);
        }
        // logger.info("-------key:" + key.toString() + "-----");
        // logger.info("-------key1:" + key1.toString() + "-----");

        if(mapEqualOr(key, key1))
        {
            return true;
        }

        return false;
    }

    /**
     * 任意一个相等就可以
     * @return
     */
    public boolean mapEqualOr(Map<String,Object> map,Map<String,Object> map1)
    {
        if(map==null || map1==null)
        {
            return false;
        }
        for(String key:map.keySet())
        {
            Object obj = map.get(key);
            Object obj1 =map1.get(key);
            if(obj==obj1)
            {
                return true;
            }
            String objStr = (obj==null)?null:obj.toString();
            String objStr1 = (obj1==null)?null:obj1.toString();

            if(objStr==null && objStr1==null)
            {
                continue;
            }
            if(objStr!=null && objStr!=null && objStr.equals(objStr1))
            {
                return true;
            }
            return false;
        }
        return true;
    }


    public void writeDataToTxt(DateTime dateTime,List<UserDailyStatBvo> totalData , FileSystem fstem) {

        String dir = srcPath;
        String osInfo= System.getProperty("os.name").toLowerCase();
        if(osInfo.indexOf("windows")>=0)
        {
            dir = winSrcPath;
        }

        String filePath = dir + getFileName(dateTime);
        String dstFilePath = dstPath + getFileName(dateTime);

        FileWriter fileWriter = null;
        boolean flag = false;
        if (totalData != null && totalData.size()>0) {
            try {
                fileWriter = getFileWriter(filePath, true);
                writeFile(dateTime,totalData, fileWriter);
                fileWriter.flush();
                flag = true;
                logger.info("数据写入本地文件完成!");
            } catch (Exception e) {
                logger.info("数据写入本地文件时出现未知异常:{}", e);
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    logger.info("关闭fileWriter失败", e);
                }
            }
            if (flag) {
                logger.info("开始上传文件啦...");
                writeHDFS(filePath,dstFilePath, fstem);
                logger.info("上传文件结束，任务完成...");
            } else {
                logger.info("写入txt文件出问题，无法上传文件...");
            }
        } else {
            logger.info("没有数据，不上传");
        }

    }


    /**
     * 将源路径的文件上传至HDFS的目标路径
     *
     * @param txtFullSrcPath
     * @param txtFullDstPath
     */
    private void writeHDFS(String txtFullSrcPath, String txtFullDstPath, FileSystem fileSystem) {
        Path p = new Path(txtFullDstPath);
        try {
            fileSystem.delete(p, true);
            logger.info("delete file:" + txtFullDstPath);
            if (!fileSystem.exists(p)) {
                fileSystem.mkdirs(p.getParent());
            }
            fileSystem.copyFromLocalFile(false, false, new Path(txtFullSrcPath), p);
        } catch (IOException e) {
            logger.info("上传文件失败", e);
        }

    }


    private void writeFile(DateTime dateTime,List<UserDailyStatBvo>  totalData, FileWriter fileWriter) {
        String createTime = dateTime.toString("yyyyMMdd");
        for (UserDailyStatBvo bvo: totalData) {
            try {
                fileWriter.append(createTime).append(separator)
                        .append((StringUtils.isBlank(bvo.getUtma())) ? "" : bvo.getUtma()).append(separator)
                        .append((StringUtils.isBlank(bvo.getUserId())) ? "" : bvo.getUserId()).append(separator)
                        .append((bvo.getPv() == null) ? "0" : bvo.getPv().toString()).append(separator)
                        .append((bvo.getOut() == null) ? "0" : bvo.getOut().toString()).append(separator)
                        .append((bvo.getOrderCount() == null) ? "0" : bvo.getOrderCount().toString()).append(separator)
                        .append((bvo.getOrderAmount() == null) ? "0" : bvo.getOrderAmount().toString()).append(separator)
                        .append((bvo.getSignCount() == null) ? "0" : bvo.getSignCount().toString())
                        .append("\n");
            } catch (IOException e) {
                logger.info("写入本地文件时出错pv:" + bvo.getPv() + ":", e);
            } catch (Exception e) {
                logger.info("写入本地文件时未知异常pv:" + bvo.getPv()+ ":", e);
            }
        }
    }

    public String getFileName(DateTime dateTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(dateTime.toString("yyyyMM"))
                .append("/")
                .append(dateTime.toString("yyyyMMdd"))
                .append("/");
        return sb.append(dateTime.toString("yyyyMMdd")).append(".txt").toString();
    }

    private FileWriter getFileWriter(String txtFullSrcPath, Boolean append) throws IOException {
        File file = new File(txtFullSrcPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        } else {
            boolean deleteSuccess = file.delete();
            if (deleteSuccess) {
                logger.info("***本地文件删除成功***", txtFullSrcPath);
            } else {
                logger.info("***存在历史文件但是删除失败***");
            }
        }
        return new FileWriter(file, append);
    }


    /**
     * 各统计项查询任务
     */
    public class StatItemTask implements Callable<Object> {

        private DateTime dateTime;

        private QueryEnum queryFlag;


        public StatItemTask(DateTime dateTime,QueryEnum  queryFlag)
        {
            this.dateTime=dateTime;
            this.queryFlag=queryFlag;
        }

        @Override
        public Object call() throws Exception {

            List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
            switch (queryFlag)
            {
                case PV:
                    result = queryPv(dateTime);
                    break;

                case OUT:
                    result = queryOut(dateTime);
                    break;
                case ORDER:
                    result = queryOrder(dateTime);
                    break;
                case SIGN:
                    result = querySign(dateTime);
                    break;
            }
            return result;
        }
    }

    /**查pv**/
    public List<Map<String,Object>> queryPv(DateTime dateTime)
    {
        String partitionTime = dateTime.toString("yyyyMMdd");
        String query = String.format(pvSql,partitionTime);
        List<Map<String, Object>> result = execSql(query);
        return result;
    }

    /**查out**/
    public List<Map<String,Object>> queryOut(DateTime dateTime)
    {
        String partitionTime = dateTime.toString("yyyyMMdd");
        String query = String.format(outSql,partitionTime);
        List<Map<String, Object>> result = execSql(query);
        return result;
    }

    /**查订单**/
    public List<Map<String,Object>> queryOrder(DateTime dateTime)
    {
        String partitionTime = dateTime.toString("yyyyMMdd");
        String query = String.format(orderSql,partitionTime);
        List<Map<String, Object>> result = execSql(query);
        return result;
    }

    /**查签到**/
    public List<Map<String,Object>> querySign(DateTime dateTime)
    {
        String partitionTime = dateTime.toString("yyyyMMdd");
        String query = String.format(signSql,partitionTime);
        List<Map<String, Object>> result = execSql(query);
        return result;
    }

    /**
     * 执行sql
     *
     * @param sql 需要执行的sql
     * @return
     */
    public List<Map<String, Object>> execSql(String sql) {
        logger.info("开始hive查询,sql:" + sql);
        List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
        try {
            rowList = jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            logger.error("HIVE：查询出错,sql:" + sql + ",ERROR:", e);
        }
        logger.info("hive查询结束,sql:" + sql);
        return rowList;
    }

    protected void setPropsPath() {
        this.propsPath = "/config/";
    }

    @Override
    protected void setLzo() {

    }

    @Override
    protected void setInputPath(FileSystem fileSystem) throws IOException {

    }

    @Override
    protected void setInputPath(FileSystem fileSystem, Object... params) throws IOException {

    }

    @Override
    protected void setOutputPath() {

    }

    @Override
    protected void setLocalPath() {

    }

    @Override
    protected void setReducsFileName() {

    }
}
