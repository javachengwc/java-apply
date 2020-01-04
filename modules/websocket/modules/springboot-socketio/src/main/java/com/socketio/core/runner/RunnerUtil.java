package com.socketio.core.runner;

import com.util.date.DateUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunnerUtil {

  private static Logger logger= LoggerFactory.getLogger(RunnerUtil.class);

  public static final String FILE_NAME="my.py";

  public static final String RUN_DIR="/tmp/python";

  public static final String WIN_RUN_DIR="D://tmp/python";

  public static final String ENCODE_UTF8 = "UTF-8";

  public static final String initInfoTemplate="\\[.*\\](#|\\$).*";

  public static final String ENTER = "\r\n";

  public static final String LINUX_ENTER="\n";

  public static final String PYTHON_CMD= "python3";

  //创建运行文件
  public static String createRunFile(Long uid,String sessionId) {
    String filePath = "";
    try{
      String dir = getRunDir();
      File dirFile = new File(dir);
      if(!dirFile.exists()) {
        dirFile.mkdirs();
      }
      String dateStr = DateUtil.formatDate(new Date(),DateUtil.FMT_YYMMDD);

      String nextPath = dateStr+File.separator+sessionId.substring(0,6).toLowerCase()+uid;
      String prePath = dir+File.separator+nextPath;
      File preDirFile = new File(prePath);
      if(!preDirFile.exists()) {
        preDirFile.mkdirs();
      }
      String fileName = FILE_NAME;
      filePath = preDirFile+ File.separator+fileName;
      File file = new File(filePath);
      try {
        if (!file.exists()) {
          file.createNewFile();
        }
      } catch (IOException e) {
        logger.error("RunnerUtil createRunFile error,uid={},sessionId={}",uid,sessionId,e);
      }

    } catch (Exception e) {
      logger.error("RunnerUtil createRunFile error,uid={},sessionId={}",uid,sessionId,e);
    }
    return filePath;
  }

  public static String getRunDir() {
    String os =System.getProperty("os.name");
    if(os!=null && os.toLowerCase().startsWith("window")) {
      return  WIN_RUN_DIR;
    }
    return RUN_DIR;
  }


  public static void writeFileContent(String filePath, String content) {
    File file = new File(filePath);
    FileOutputStream out = null;
    try {
      out =new FileOutputStream(file);
      IOUtils.write(content, out, ENCODE_UTF8);
      out.flush();
    } catch(IOException e) {
      logger.error("PythonExecutor setFileContent error,filePath={},",filePath,e);
      throw new RuntimeException(e);
    } finally {
      IOUtils.closeQuietly(out);
    }
  }

}
