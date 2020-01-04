package com.socketio.core.runner;

import com.pty4j.util.PtyUtil;
import com.socketio.core.SocketioClientFactory;
import com.socketio.core.SocketioEvent;
import com.socketio.core.SocketioSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//原生的pty
@Data
public class OrglPtyRunner implements IPtyRunner {

  private static Logger logger= LoggerFactory.getLogger(OrglPtyRunner.class);

  private static Map<String,String> envs =new HashMap<String,String>(System.getenv());

  private Long uid ;

  private String sessionId;

  private String workDir="";

  private Process process;

  private OutputStreamWriter writer;

  private InputStreamReader reader;

  private SocketioSession socketioSession;

  private RunThread runThread;

  private volatile boolean inited=false;

  //是否下发过信息
  private volatile boolean hasSendFirst=false;

  private volatile boolean canInut =false;

  public OrglPtyRunner() {

  }

  public OrglPtyRunner(SocketioSession socketioSession) {
    this.uid=socketioSession.getUid();
    this.sessionId= socketioSession.getSessionId();
    this.socketioSession =socketioSession;
    socketioSession.setPtyRunner(this);
  }

  public String [] getSysCmd() {
    String [] command=null;
    String os =System.getProperty("os.name");
    if(os!=null && os.toLowerCase().startsWith("window")) {
      command = new String[]{"cmd.exe"};
    }else  {
      command = new String[]{"/bin/bash","--login"};
      envs.put("TERM", "linux");
    }
    return command;
  }

  public String getEnter() {
    String os =System.getProperty("os.name");
    if(os!=null && os.toLowerCase().startsWith("window")) {
      return RunnerUtil.ENTER;
    }else  {
      return RunnerUtil.LINUX_ENTER;
    }
  }

  @Override
  public void resize(String size) {

  }

  //初始化
  @Override
  public void init() {
    logger.info("OrglPtyRunner init start,uid={},sessionId={}",uid,sessionId);

    String runFile = RunnerUtil.createRunFile(uid,sessionId);
    workDir=runFile.substring(0,runFile.lastIndexOf(File.separator));
    logger.info("OrglPtyRunner init workDir={}",workDir);
    String [] cmd = this.getSysCmd();
    logger.info("OrglPtyRunner init cmd={}",cmd);
    try {
      String [] envArray =PtyUtil.toStringArray(envs);
      process = Runtime.getRuntime().exec(cmd,envArray,new File(workDir));
      logger.info("OrglPtyRunner init ptyProcess start");

      InputStream inputStream = process.getInputStream();
      OutputStream outputStream = process.getOutputStream();

      reader = new InputStreamReader(inputStream);
      writer = new OutputStreamWriter(outputStream);

      this.genRunThread();
      logger.info("OrglPtyRunner init genRunThread end ");
      this.genProcessOutput();
      logger.info("OrglPtyRunner init genProcessOutput end ");

    } catch (Exception e) {
      logger.error("OrglPtyRunner init ptyProcess error,uid={},sessionId={}",uid,sessionId,e);
    }
    inited=true;
    logger.info("OrglPtyRunner init end,uid={},sessionId={}",uid,sessionId);
  }

  @Override
  public void destroy() {
    try {
      if (process.isAlive()) {
        process.destroy();
      }
      if(runThread!=null && runThread.isAlive()) {
        runThread.interrupt();
      }
    }catch (Exception e) {
      logger.error("OrglPtyRunner destroy error,uid={}",uid,e);
    }
  }

  @Override
  public boolean exec( String code) {
    logger.info("OrglPtyRunner exec start, uid={},code={}",uid,code);
    String filePath =workDir+ File.separator+RunnerUtil.FILE_NAME;
    File file = new File(filePath);
    try {
      if (!file.exists()) {
        file.createNewFile();
      }
    } catch (IOException e) {
      logger.error("OrglPtyRunner exec 创建文件 error,filePath={},",filePath,e);
      return false;
    }

    //写代码
    RunnerUtil.writeFileContent(filePath,code);
    logger.info("OrglPtyRunner exec writeFileContent");

    try {
      writer.write(RunnerUtil.PYTHON_CMD+" "+file);
      writer.write(getEnter());
      writer.flush();
      logger.info("OrglPtyRunner exec end,code={}",code);
    }catch (Exception e) {
      logger.error("OrglPtyRunner exec write error",e);
      return false;
    }
    return true;
  }

  @Override
  public boolean input(String param) {
    logger.info("OrglPtyRunner input start, uid={},param={}",uid,param);
    try {
      if(param.endsWith(RunnerUtil.ENTER) && RunnerUtil.LINUX_ENTER.equals(getEnter()) ) {
        param = param.replace(RunnerUtil.ENTER,RunnerUtil.LINUX_ENTER);
      }
      writer.write(param);
      writer.flush();
    }catch (Exception e) {
      logger.error("OrglPtyRunner input writer write error",e);
      return false;
    }
    return true;
  }

  public  void  genRunThread() {
    RunThread runThread = new RunThread(uid,sessionId,process);
    runThread.start();
  }

  public void genProcessOutput( ) {
    Thread stdoutReader = new ReaderThread(reader,this);
    stdoutReader.start();

    Thread errorReader = new ReaderThread(new InputStreamReader(process.getErrorStream()),this);
    errorReader.start();
    logger.info("OrglPtyRunner genProcessOutput thread start");
  }


  @Override
  public boolean isFirstInfo() {
    return !hasSendFirst;
  }

  @Override
  public void firstSended() {
    hasSendFirst=true;
  }

  @Override
  public void setCanInput(boolean can) {
    this.canInut= can;
  }

  private static class RunThread extends Thread {

    private Long uid;

    private String sessionId;

    private Process process;

    RunThread(Long uid,String sessionId,Process process) {
      this.uid = uid;
      this.sessionId= sessionId;
      this.process = process;
    }

    @Override
    public void run() {
      logger.info("OrglPtyRunner RunThread run start,uid={},sessionId={}",uid,sessionId);
      try {
        process.waitFor();
      } catch (Exception e) {
        logger.error("OrglPtyRunner RunThread run error,uid={},sessionId={}",uid,sessionId,e);
      }
    }

  }

  private static class ReaderThread extends Thread {

    private Reader reader;

    private IPtyRunner ptyRunner;

    ReaderThread(Reader reader,IPtyRunner ptyRunner) {
      this.reader = reader;
      this.ptyRunner =ptyRunner;
    }

    @Override
    public void run() {
      String sessionId = ptyRunner.getSessionId();
      Long uid = ptyRunner.getUid();
      logger.info("OrglPtyRunner ReaderThread run start,uid={},sessionId={}",uid,sessionId);
      try {
        char[] buf = new char[10 * 1024];
        while (true) {
          int count = reader.read(buf);
          if (count < 0) {
            return;
          }
          String message = new String(buf, 0, count);
          logger.info("OrglPtyRunner ReaderThread run message={}",message);
          SocketioSession socketioSession = SocketioClientFactory.getBySession(sessionId);
          if(socketioSession==null) {
            logger.info("OrglPtyRunner ReaderThread socketioSession is null,uid={},sessionId={}",uid,sessionId);
            continue;
          }
          if(ptyRunner.isFirstInfo()) {
            logger.info("OrglPtyRunner ReaderThread send first message,uid={},sessionId={},message={}",
                uid,sessionId,message);
            //初次下发信息
            socketioSession.sendEventMessage(SocketioEvent.DOWN_INIT, message);
            ptyRunner.firstSended();
          } else {
            logger.info("OrglPtyRunner ReaderThread send,uid={},sessionId={}",uid,sessionId);
            socketioSession.sendEventMessage(SocketioEvent.DOWN_INFO, message);
            if(message!=null && message.matches(RunnerUtil.initInfoTemplate)) {
              ptyRunner.setCanInput(false);
            }
          }
        }
      } catch (Exception e) {
        logger.error("PtyRunner2 ReaderThread run error,uid={}",uid,e);
      }
    }

  }

}
