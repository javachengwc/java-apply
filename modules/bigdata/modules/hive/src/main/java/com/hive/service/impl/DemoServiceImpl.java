package com.hive.service.impl;

import com.hive.service.DemoService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 例子服务类
 */
@Service
public class DemoServiceImpl implements DemoService {

    private static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    @Qualifier("jdbcTemplateHive")
    private JdbcTemplate jdbcTemplate;

    //hdfs文件路径
    //文件内容格式: PID11\00130\r\nPID12\00110 换行符\r\n 分割符\001
    private String hdfsFilePath;

    private String tableName = "data_stat";

    private String columnDefine = "stat_date String, product_id String,cnt int";

    public String getHdfsFilePath() {
        return hdfsFilePath;
    }

    public void setHdfsFilePath(String hdfsFilePath) {
        this.hdfsFilePath = hdfsFilePath;
    }

    public void saveHdfsToHive(DateTime statDate)
    {
        try {
            String partitionTime = statDate.toString("yyyyMMdd");
            createPartition(hdfsFilePath  + partitionTime,tableName,columnDefine,partitionTime);
        } catch (Exception e) {
            logger.error("DemoServiceImpl saveHdfsToHive error,statDate="+statDate.toString(),e);
        }
    }

    public boolean createPartition(String path, String tableName, String colunmDefined, String partition){
        try {

            String createTableSql = "create external table if not exists " + tableName + "(" + colunmDefined + ")" +
                    " partitioned by (partition string) " +
                    " STORED AS " +
                    " INPUTFORMAT 'com.hive.util.DemoInputFormat' " +
                    " OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat '";

            String dropPartition = "alter table " + tableName + " drop partition(partition='" + partition + "')";

            String alertSql = "alter table " + tableName + " add partition(partition='" + partition + "') location '" + path + "'";


            logger.info("DemoServiceImpl createPartition create sql = " + createTableSql);
            jdbcTemplate.execute(createTableSql);

            try {
                logger.info("DemoServiceImpl createPartition drop partition sql = " + dropPartition);
                jdbcTemplate.execute(dropPartition);
            } catch (Exception e) {
                logger.info("DemoServiceImpl createPartition drop partition error,dropPartition="+dropPartition,e);
            }

            logger.info("DemoServiceImpl createPartition alert partition sql = " + alertSql);
            jdbcTemplate.execute(alertSql);

            return true;
        } catch (Exception e) {
            logger.error("DemoServiceImpl createPartition error", e);
            return false;
        }
    }
}
