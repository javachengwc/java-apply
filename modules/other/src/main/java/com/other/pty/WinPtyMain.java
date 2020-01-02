package com.other.pty;

import com.pty4j.PtyProcessBuilder;
import com.pty4j.windows.WinPtyProcess;
//import com.sun.jna.Platform;

import java.io.*;

import com.google.common.base.Charsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jvnet.winp.WinProcess;
import org.jvnet.winp.WinpException;

public class WinPtyMain {

    private static final String ENTER = "\r\n";

    private static final String LINUX_ENTER="\n";

    public static String [] getSysCmd() {
        String [] command=null;
        String os =System.getProperty("os.name");
        if(os!=null && os.toLowerCase().startsWith("window")) {
            command = new String[]{"cmd.exe"};
        }else  {
            command = new String[]{"/bin/bash", "--login"};
        }
        return command;
    }

    public static void main(String [] args) throws Exception {

        //String [] cmd ={"java","A"};

        String[] cmd = new String[]{"cmd.exe"};
        Map<String,String> envs = new HashMap<String,String>(System.getenv());

        String workingDir = new File("D:\\tmp\\python").getAbsolutePath();
        PtyProcessBuilder builder = new PtyProcessBuilder(cmd)
                .setDirectory(workingDir)
                .setEnvironment(envs)
                .setConsole(false)
                .setCygwin(false);
        WinPtyProcess process = (WinPtyProcess) builder.start();
        String workingDirectory = getWorkingDirectory(process);
        System.out.println(workingDirectory);
        System.out.println("-------------------------------");

        printProcessOutput(process);

        Thread.sleep(2000);
        OutputStream outputStream =process.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        writer.write("python aaa.py ");
        writer.flush();
        Thread.sleep(1000);
        writer.write(ENTER);
        writer.flush();
        Thread.sleep(1000);
        writer.flush();

        Thread.sleep(3000);
        writer.write("abcdefg ");
        writer.flush();
        Thread.sleep(3000);
        writer.write("hijklmn ");
        writer.flush();
        Thread.sleep(3000);
        writer.write(ENTER);
        writer.flush();
        writer.close();

        process.waitFor();
        Thread.sleep(1000);
        process.destroy();
    }

    public static String getWorkingDirectory( WinPtyProcess process) throws IOException {
        try {
            return process.getWorkingDirectory();
        } catch (IOException e) {
            throw e;
        }
    }

    public static void printProcessOutput( Process process) {

        Thread stdoutReader = new ReaderThread(new InputStreamReader(process.getInputStream()), System.out);
        Thread stderrReader = new ReaderThread(new InputStreamReader(process.getErrorStream()), System.out);
        stdoutReader.start();
        stderrReader.start();
    }

    private static String getExecutable(int pid) {
        WinProcess winProcess = new WinProcess(pid);
        String commandLine;
        try {
            commandLine = winProcess.getCommandLine();
        }
        catch (WinpException e) {
            e.printStackTrace(System.out);
            return null;
        }
        return commandLine;
    }

    private static class ReaderThread extends Thread {
        private final Reader myIn;
        private final PrintStream myOut;

        ReaderThread(Reader in, PrintStream out) {
            myIn = in;
            myOut = out;
        }

        @Override
        public void run() {
            try {
                char[] buf = new char[32 * 1024];
                while (true) {
                    int count = myIn.read(buf);
                    if (count < 0) {
                        return;
                    }
                    String message = new String(buf, 0, count);
                    System.out.println("-------------"+message);
                    myOut.print(message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
