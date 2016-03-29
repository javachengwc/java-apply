package com.hadoop.task.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public abstract class HadoopBaseTask {

    private Logger logger = LoggerFactory.getLogger(HadoopBaseTask.class);

    protected Properties properties;

    protected FileSystem fileSystem;

    protected Configuration configuration;

    protected String separator = System.getProperty("file.separator");

    protected String propsPath;

    protected String inputPath;

    protected String outputPath;

    protected String localPath;

    protected String reducsFileName;

    protected boolean isLzo = false;

    protected abstract void setPropsPath();

    protected abstract void setLzo();

    /**
     * 初始化hadoop客户端
     */
    protected boolean initHadoopClient() {
        try {
            setPropsPath();
            setLzo();

            fileSystem = buildFileSystem();
            initProperties();
        } catch (Exception e) {

            logger.error("initHadoopClient error:", e);
            return false;
        }
        return true;
    }

    /**
     * 构造hdfs文件系统
     */
    protected FileSystem buildFileSystem() {
        try {
            if (propsPath != null && !propsPath.endsWith(separator)) {
                propsPath += separator;
            }

            configuration = new Configuration();

            configuration.addResource(Thread.currentThread().getContextClassLoader().getResource(propsPath + "core-site.xml"));
            configuration.addResource(Thread.currentThread().getContextClassLoader().getResource(propsPath + "hdfs-site.xml"));
            configuration.addResource(Thread.currentThread().getContextClassLoader().getResource(propsPath + "mapred-site.xml"));
            configuration.addResource(Thread.currentThread().getContextClassLoader().getResource(propsPath + "Yarn-site.xml"));

            if (isLzo) {
                //当客户端提交lzo 文件job时需要使用以下配置
                configuration.set("io.compression.codecs", "org.apache.hadoop.io.compress.DefaultCodec,org.apache.hadoop.io.compress.GzipCodec,com.hadoop.compression.lzo.LzopCodec");
                configuration.set("io.compression.codec.lzo.class", "com.hadoop.compression.lzo.LzoCodec");
            }

            return FileSystem.get(configuration);
        } catch (Exception e) {
            logger.error("初始化FileSystem失败.", e);
        }
        return null;
    }

    /**
     * 初始化properties
     */
    protected void initProperties() throws Exception {
        if (propsPath != null && !propsPath.endsWith(separator)) {
            propsPath += separator;
        }

        properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propsPath + "job.properties"));
    }

    /**
     * 获取输入文件路径
     */
    protected abstract void setInputPath(FileSystem fileSystem) throws IOException;

    /**
     * 获取输入文件路径
     */
    protected abstract void setInputPath(FileSystem fileSystem, Object... params) throws IOException;

    /**
     * 输出目录
     */
    protected abstract void setOutputPath();

    /**
     * 本文文件路径
     */
    protected abstract void setLocalPath();

    /**
     * reduce文件名
     */
    protected abstract void setReducsFileName();

    /**
     * 检查路径结尾是否含有分隔符，没有自动加上
     */
    protected void isEndWithSeparator(String path) {

        if (StringUtils.isEmpty(path)) {
            return;
        }

        if (!path.endsWith(separator)) {
            path += separator;
        }
    }

}
