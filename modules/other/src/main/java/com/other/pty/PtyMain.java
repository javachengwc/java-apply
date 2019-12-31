package com.other.pty;

import com.pty4j.PtyProcess;
import com.pty4j.WinSize;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PtyMain {

  private static Logger logger = LoggerFactory.getLogger(PtyMain.class);

  private static final String ENTER = "\r\n";

  public static void main(String [] args) throws Exception {

    String dir="E:\\tmp";

    Map<String, String> envs = new HashMap<>(System.getenv());
    envs.put("TERM", "xterm");
    String[] command = new String[]{"cmd.exe"};
    PtyProcess ptyProcess = PtyProcess.exec(command, envs, null);
    InputStreamReader in = new InputStreamReader(ptyProcess.getInputStream());
    OutputStream  out = ptyProcess.getOutputStream();

    ptyProcess.setWinSize(new WinSize(100, 100));

  }
//
//  public void disconnect() throws IOException {
//    if (ptyProcess != null) {
//      sendBytes(new byte[]{9});
//      sendString("EXIT");
//      ptyProcess.destroy();
//    }
//  }
//
//  public void resize(int columns, int rows) {
//    if (ptyProcess != null && ptyProcess.isRunning()) {
//
//    }
//  }
//
//  public void sendString(String str) throws IOException {
//    out.write(str.getBytes());
//    if (!str.endsWith(ENTER)) {
//      out.write(ENTER.getBytes());
//    }
//    out.flush();
//  }
//
//  public void sendBytes(byte[] bytes) throws IOException {
//    out.write(bytes);
//    out.flush();
//  }
//
//  public int read(char[] buffer) throws IOException {
//    return in.read(buffer);
//  }
//
//  public boolean isRunning() {
//    return ptyProcess != null && ptyProcess.isRunning();
//  }

}
