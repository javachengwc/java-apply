package com.manageplat.service.sync;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetNotificationHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
import org.springframework.stereotype.Service;

@Service
public class TelnetService implements Runnable, TelnetNotificationHandler {

    public TelnetService() {
        super();
    }

    private static TelnetClient tc = null;
    private InputStream in;
    private PrintStream out;

    @Override
    public void receivedNegotiation(int negotiation_code, int option_code) {
        String command = null;
        if (negotiation_code == TelnetNotificationHandler.RECEIVED_DO) {
            command = "DO";
        } else if (negotiation_code == TelnetNotificationHandler.RECEIVED_DONT) {
            command = "DONT";
        } else if (negotiation_code == TelnetNotificationHandler.RECEIVED_WILL) {
            command = "WILL";
        } else if (negotiation_code == TelnetNotificationHandler.RECEIVED_WONT) {
            command = "WONT";
        }
        System.out.println("Received " + command + " for option code " + option_code);
    }

    public void execCommand(String ip, String username, String password, String command) {
        Thread t = new Thread(new TelnetService(ip, 23, username, password, command));
        t.start();
    }

    @Override
    public void run() {
        InputStream instr = tc.getInputStream();
        try {
            byte[] buff = new byte[1024];
            int ret_read = 0;
            do {
                ret_read = instr.read(buff);
                if (ret_read > 0) {
                    System.out.print(new String(buff, 0, ret_read));
                }
            } while (ret_read >= 0);
        } catch (Exception e) {
            System.err.println("Exception while reading socket:" + e.getMessage());
        }
        try {
            tc.disconnect();
        } catch (Exception e) {
            System.err.println("Exception while closing telnet:" + e.getMessage());
        }
    }

    public TelnetService(String ip, int port, String username, String password, String command) {
        if (intconnect(ip, port)) {
            write(username);
            write(password);
            write(command);
        }
    }

    private void write(String command) {
        try {
            out.println(command);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean intconnect(String ip, int port) {
        try {
            tc = new TelnetClient();
            TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);
            EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);
            SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);

            tc.addOptionHandler(ttopt);
            tc.addOptionHandler(echoopt);
            tc.addOptionHandler(gaopt);
            tc.connect(ip, port);
            in = tc.getInputStream();
            out = new PrintStream(tc.getOutputStream());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                tc.disconnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }

}
