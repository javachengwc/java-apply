package com.other.shell;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.stream.LogOutputStream;
import org.zeroturnaround.exec.stream.slf4j.Slf4jStream;

public class Exec {

  public static void main(String [] args ) throws Exception {

    System.out.println("Exec main start........");
    ProcessExecutor processExecutor = new ProcessExecutor();
    processExecutor.readOutput(true);
    ProcessResult processResult =processExecutor.command("java", "-version").execute();
    System.out.println(processResult.outputString());
    processExecutor.destroyOnExit();

    int exit = new ProcessExecutor().command("java", "-version").execute().getExitValue();
    System.out.println(exit);

    String output = new ProcessExecutor().command("java", "-version").readOutput(true).execute().outputUTF8();
    System.out.println(output);

    ProcessExecutor exec=new ProcessExecutor().command("ping", "www.baidu.com","-t").timeout(10, TimeUnit.SECONDS);
    try {
      exec.redirectOutput(new LogOutputStream() {
        @Override
        protected void processLine(String line) {
          System.out.println("----------------" + line);
        }
      }).execute();
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }

    try {
      new ProcessExecutor().command("java", "-version").timeout(60, TimeUnit.SECONDS).execute();
    }
    catch (TimeoutException e) {
      // process is automatically destroyed
    }

    Future<ProcessResult> future = new ProcessExecutor()
        .command("java", "-version")
        .readOutput(true)
        .start().getFuture();
    String result = future.get(60, TimeUnit.SECONDS).outputUTF8();
    System.out.println(output);
  }

}
