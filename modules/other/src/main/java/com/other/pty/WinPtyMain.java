package com.other.pty;

import com.pty4j.PtyProcessBuilder;
import com.pty4j.windows.WinPtyProcess;
//import com.sun.jna.Platform;

import java.io.*;

import com.google.common.base.Charsets;
import org.jvnet.winp.WinProcess;
import org.jvnet.winp.WinpException;

public class WinPtyMain {

    public static void main(String [] args) throws Exception {
//        WinPtyProcess.joinCmdArgs(new String[]{
//                "C:\\Python35\\python.exe",
//                "C:\\Program Files (x86)\\JetBrains\\PyCharm 5.0.2\\helpers\\pydev\\pydevd.py"
//        });

//        String [] command=null;
//        if (OsUtil.isWindows) {
//            command = new String[]{"cmd.exe"};
//        } else {
//            command = new String[]{"/bin/bash", "--login"};
//            envs.put("TERM", "xterm");
//        }
//

        //String [] cmd ={"java","A"};

        String[] cmd = new String[]{"cmd.exe"};

        String workingDir = new File("E:\\tmp").getAbsolutePath();
        PtyProcessBuilder builder = new PtyProcessBuilder(cmd)
                .setDirectory(workingDir)
                .setEnvironment(System.getenv())
                .setConsole(false)
                .setCygwin(false);
        WinPtyProcess process = (WinPtyProcess) builder.start();
        String workingDirectory = getWorkingDirectory(process);
        System.out.println(workingDirectory);

        printProcessOutput(process);

        OutputStream outputStream =process.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "utf-8");
        writer.write("ping www.baidu.com -t \r\n");
        writer.flush();
        writer.close();

        //printProcessOutput(process);

        process.waitFor();
        Thread.sleep(1000);
        process.destroy();
    }

    public static String getWorkingDirectory( WinPtyProcess process) throws IOException {
        try {
            return process.getWorkingDirectory();
        }
        catch (IOException e) {
//            if (!Platform.is64Bit()) {
//                return null;
//            }
            throw e;
        }
    }

    public static void printProcessOutput( Process process) {
        Thread stdoutReader = new ReaderThread(new InputStreamReader(process.getInputStream(), Charsets.UTF_8), System.out);
        Thread stderrReader = new ReaderThread(new InputStreamReader(process.getErrorStream(), Charsets.UTF_8), System.err);
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
        //return ContainerUtil.getFirstItem(ParametersListUtil.parse(commandLine));
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
                    myOut.print(new String(buf, 0, count));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
