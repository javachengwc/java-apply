package com.socketio.core.runner;

import com.pty4j.PtyProcess;
import com.pty4j.PtyProcessBuilder;
import com.pty4j.WinSize;
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
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Data
@EqualsAndHashCode
public class PtyRunner implements IPtyRunner {

  private static Logger logger= LoggerFactory.getLogger(PtyRunner.class);

  private static Map<String,String> envs =new HashMap<String,String>(System.getenv());

  private Long uid ;

  private String sessionId;

  private String workDir="";

  private PtyProcess ptyProcess;

  private OutputStreamWriter writer;

  private InputStreamReader reader;

  private SocketioSession socketioSession;

  private RunThread runThread;

  private String filePath;

  private volatile boolean inited=false;

  private volatile boolean hasSendFirst=false;

  private volatile boolean canInut =false;

  public PtyRunner() {

  }

  public PtyRunner(SocketioSession socketioSession) {
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
      command = new String[]{"/bin/bash", "--login"};
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

  //初始化
  @Override
  public void init() {
    logger.info("PtyRunner init start,uid={},sessionId={}",uid,sessionId);
    String runFile= RunnerUtil.createRunFile(uid,sessionId);
    workDir = runFile.substring(0, runFile.lastIndexOf(File.separator));
    logger.info("PtyRunner init workDir={}", workDir);
    String[] cmd = this.getSysCmd();
    PtyProcessBuilder builder = new PtyProcessBuilder(cmd)
        .setDirectory(workDir)
        .setEnvironment(envs)
        .setConsole(false)
       // .setInitialColumns()
       // .setInitialRows()
        .setRedirectErrorStream(true)
       //.setWindowsAnsiColorEnabled(false)
        .setCygwin(false);
    try {
      ptyProcess = builder.start();
      logger.info("PtyRunner init ptyProcess start");
      InputStream inputStream = ptyProcess.getInputStream();
      OutputStream outputStream = ptyProcess.getOutputStream();

      reader = new InputStreamReader(inputStream);
      writer = new OutputStreamWriter(outputStream);
      this.genProcessOutput();
      logger.info("PtyRunner init genProcessOutput end ");
      this.genRunThread();
      logger.info("PtyRunner init genRunThread end ");
    } catch (Exception e) {
      logger.error("PtyRunner init ptyProcess error,uid={},sessionId={}",uid,sessionId, e);
    }
    inited=true;
    logger.info("PtyRunner init end,uid={},sessionId={}",uid,sessionId);
  }

  @Override
  public void destroy() {
    try {
      if (ptyProcess.isRunning() || ptyProcess.isAlive()) {
        ptyProcess.destroy();
      }
      if(runThread!=null && runThread.isAlive()) {
        runThread.interrupt();
      }
    }catch (Exception e) {
      logger.error("PtyRunner destroy error,uid={}",uid,e);
    }
  }

  @Override
  public void resize(String size) {
    logger.info("PtyRunner resize start, uid={},sessionId={},size={}",uid,sessionId,size);
    String [] ps = size.split(",");
    Integer cols = Integer.parseInt(ps[0]);
    Integer rows = Integer.parseInt(ps[1]);
    WinSize winSize = new WinSize(cols,rows);
    ptyProcess.setWinSize(winSize);
    logger.info("PtyRunner resize end,uid={},sessionId={}",uid,sessionId);
  }

  @Override
  public boolean exec( String code) {
    logger.info("PtyRunner exec start, uid={},sessionId={},code={}",uid,sessionId,code);
    filePath =workDir+ File.separator+RunnerUtil.FILE_NAME;
    File file = new File(filePath);
    try {
      if (!file.exists()) {
        file.createNewFile();
      }
    } catch (IOException e) {
      logger.error("PtyRunner exec 创建文件 error,filePath={},",filePath,e);
      return false;
    }

    //写代码
    RunnerUtil.writeFileContent(filePath,code);
    logger.info("PtyRunner exec writeFileContent");

    try {
      writer.write(RunnerUtil.PYTHON_CMD+" "+file);
      writer.write(getEnter());
      writer.flush();
      canInut=true;
      logger.info("PtyRunner exec end,code={}",code);
    }catch (Exception e) {
      logger.error("PtyRunner exec write error",e);
      return false;
    }
    return true;
  }

  @Override
  public boolean input(String param) {
    logger.info("PtyRunner input start, uid={},param={}",uid,param);
    try {
      if(!canInut) {
        return false;
      }
      if(param.endsWith(RunnerUtil.ENTER) && RunnerUtil.LINUX_ENTER.equals(getEnter()) ) {
        param = param.replace(RunnerUtil.ENTER,RunnerUtil.LINUX_ENTER);
      }
      writer.write(param);
      writer.flush();
    }catch (Exception e) {
      logger.error("PtyRunner input write error",e);
      return false;
    }
    return true;
  }

  public  void  genRunThread() {
    RunThread runThread = new RunThread(uid,sessionId,ptyProcess);
    runThread.start();
    logger.info("PtyRunner genRunThread thread start");
  }

  public void genProcessOutput( ) {
    Thread stdoutReader = new ReaderThread(reader,this);
    stdoutReader.start();
    logger.info("PtyRunner genProcessOutput thread start");
  }

  private static class RunThread extends Thread {

    private Long uid;

    private String sessionId;

    private PtyProcess ptyProcess;

    RunThread(Long uid,String sessionId,PtyProcess ptyProcess) {
      this.uid = uid;
      this.sessionId= sessionId;
      this.ptyProcess = ptyProcess;
    }

    @Override
    public void run() {
      logger.info("PtyRunner RunThread run start,uid={},sessionId={}",uid,sessionId);
      try {
        ptyProcess.waitFor();
      } catch (Exception e) {
        logger.error("PtyRunner RunThread run error,uid={},sessionId={}",uid,sessionId,e);
      }
    }

  }

  private static class ReaderThread extends Thread {

    private Reader reader;

    private PtyRunner ptyRunner;

    ReaderThread(Reader reader,PtyRunner ptyRunner) {
      this.reader = reader;
      this.ptyRunner =ptyRunner;
    }

    @Override
    public void run() {
      Long uid = ptyRunner.getUid();
      String sessionId = ptyRunner.getSessionId();
      logger.info("PtyRunner ReaderThread run start,uid={},sessionId={}",uid,sessionId);
      try {
        char[] buf = new char[10 * 1024];
        while (true) {
          int count = reader.read(buf);
          if (count < 0) {
            return;
          }
          String message = new String(buf, 0, count);
          logger.info("PtyRunner ReaderThread run message={}",message);
          SocketioSession socketioSession = SocketioClientFactory.getBySession(sessionId);
          if(socketioSession==null) {
            logger.info("PtyRunner ReaderThread socketioSession is null,uid={},sessionId={}",uid,sessionId);
            continue;
          }
          if(ptyRunner.isFirstInfo()) {
            logger.info("PtyRunner ReaderThread send first message,uid={},sessionId={},message={}",
                uid,sessionId,message);
            //初次下发信息
            socketioSession.sendEventMessage(SocketioEvent.DOWN_INIT, message);
            ptyRunner.firstSended();
          } else {
            logger.info("PtyRunner ReaderThread send,uid={},sessionId={}",uid,sessionId);
            socketioSession.sendEventMessage(SocketioEvent.DOWN_INFO, message);
            if(message!=null && message.matches(RunnerUtil.initInfoTemplate)) {
              ptyRunner.setCanInput(false);
            }
          }
        }
      } catch (Exception e) {
        logger.error("PtyRunner ReaderThread run error,uid={}",uid,e);
      }
    }

  }


}
