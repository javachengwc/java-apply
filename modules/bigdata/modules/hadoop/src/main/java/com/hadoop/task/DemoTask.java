package com.hadoop.task;

import com.hadoop.job.DemoJob;
import com.hadoop.service.DemoService;
import com.hadoop.task.base.HadoopBaseTask;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 */
@Component
public class DemoTask extends HadoopBaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoTask.class);

    @Autowired
    private DemoService demoService;

    private Object exec(DateTime dateTime) {

        LOGGER.info(dateTime.toString("yyyy-MM-dd HH:mm:ss") + ",DemoTask running...");

        boolean initSucc = initHadoopClient();

        if (!initSucc) {
            LOGGER.error("初始化hadoop客户端失败!");
            return null;
        }

        try {
            String yearAndMonth =""+dateTime.getYear()+dateTime.getMonthOfYear();
            String today = ""+dateTime.getDayOfMonth();
            String hour = "" + dateTime.getHourOfDay();

            // 获取hadoop文件
            setInputPath(fileSystem, yearAndMonth, today, hour);

            if (StringUtils.isEmpty(inputPath)) {
                LOGGER.info("没有输入文件,输入路径为:" + inputPath);
                return null;
            }

            // 设置输出文件目录
            setOutputPath();

            setLocalPath();

            // 设置输出文件
            setReducsFileName();

            if (StringUtils.isEmpty(outputPath)) {
                LOGGER.error("输出文件路径为空!");
                return null;
            }

            Path output = new Path(outputPath);
            if (fileSystem.exists(output)) {
                fileSystem.delete(output, true);
            }

            // run job
            DemoJob job = new DemoJob();
            boolean isSucc = job.run(configuration, inputPath, output);
            if (isSucc) {
                Path remoteFile = new Path(outputPath + reducsFileName);

                String localFileFullPath = localPath + reducsFileName;
                Path localFile = new Path(localFileFullPath);

                fileSystem.copyToLocalFile(remoteFile, localFile);

                // 解析文件入库
                Object rt= demoService.findFromReduceFile(localFileFullPath, dateTime);
                return rt;

            }
            return null;
        } catch (Exception e) {

            LOGGER.error(dateTime.toString("yyyy-MM-dd HH:mm:ss") + ",DemoTask error!", e);
            return null;

        }finally {

            LOGGER.info(dateTime.toString("yyyy-MM-dd HH:mm:ss") + ",DemoTask end...");
        }
    }


    @Override
    protected void setPropsPath() {
        this.propsPath = "/config/";
    }

    @Override
    protected void setLzo() {
        this.isLzo = true;
    }

    @Override
    protected void setInputPath(FileSystem fileSystem) throws IOException {

    }

    @Override
    protected void setInputPath(FileSystem fileSystem, Object... params) throws IOException {

        //这里都是hadoop系统文件路径
        String inputPaths = properties.getProperty("demo.inputDir");
        List<FileStatus> fileStatuses = new ArrayList<FileStatus>();

        String yearAndMonth = params[0].toString();
        String today = params[1].toString();
        String hour = params[2].toString();

        for (String inputPath : inputPaths.split(";")) {
            isEndWithSeparator(inputPath);

            inputPath += yearAndMonth + "/" + today + "/";
            LOGGER.info("inputPath:" + inputPath);

            FileStatus[] pathFiles = fileSystem.listStatus(new Path(inputPath));
            if (pathFiles != null) {
                fileStatuses.addAll(Arrays.asList(pathFiles));
            }
        }
        if (fileStatuses.isEmpty()) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        List<String> pathList = new ArrayList<String>();
        for (int i = 0; i < fileStatuses.size(); i++) {
            Integer hourOfFile = getHourFromFileName(fileStatuses.get(i).getPath().getName());
            if (fileStatuses.get(i).getPath().toUri().getPath().endsWith(".lzo")
                    && hourOfFile<=Integer.parseInt(hour)
                    && fileStatuses.get(i).getLen() != 0) {
                pathList.add(fileStatuses.get(i).getPath().toUri().getPath());
            }
        }
        for (int i = 0; i < pathList.size(); i++) {
            if (i == pathList.size() - 1) {
                stringBuilder.append(pathList.get(i));
            } else {
                stringBuilder.append(pathList.get(i)).append(",");
            }
        }

        this.inputPath = stringBuilder.toString();
    }

    @Override
    protected void setLocalPath() {
        localPath = properties.getProperty("demo.localDir");
        isEndWithSeparator(localPath);
    }

    @Override
    protected void setOutputPath() {
        outputPath = properties.getProperty("demo.outputDir");
        isEndWithSeparator(outputPath);
    }

    @Override
    protected void setReducsFileName() {
        this.reducsFileName = properties.getProperty("default.fileName");
    }

    public static void main(String[] args) throws Exception {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/ApplicationContext*.xml");

        DemoTask demoTask = applicationContext.getBean(DemoTask.class);

        try {
            demoTask.exec(new DateTime());
        } catch (Exception e){
            LOGGER.error("demoTask is error",e);
        } finally {
            System.exit(0);
        }

    }

    /**
     * 获取文件中的小时
     */
    private Integer getHourFromFileName(String fileName) {
        Pattern pattern = Pattern.compile("(?<=-)\\d{2}");
        if(!fileName.contains("-")){
            pattern = Pattern.compile("\\d{2}(?=\\.)");
        }
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0));
        }
        return null;
    }
}

