package com.manageplat.util;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * telnet工具类
 */
public class TelnetUtil {

    private TelnetClient telnet = new TelnetClient("VT220");// telnet有VT100 VT52 VT220 VTNT ANSI等协议。

    private InputStream in;

    private PrintStream out;

    private static final String DEFAULT_AIX_PROMPT = "C:\\Users\\Administrator>";

    // telnet 端口
    private String port;

    // 用户名
    private String user;

    // 密码
    private String password;

    // IP 地址
    private String ip;

    // 缺省端口
    private static final int DEFAULT_TELNET_PORT = 23;

    public TelnetUtil(String ip, String user, String password) {
        this.ip = ip;
        this.port = String.valueOf(TelnetUtil.DEFAULT_TELNET_PORT);
        this.user = user;
        this.password = password;
    }

    public TelnetUtil(String ip, String port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    /**
     * @return boolean 连接成功返回true，否则返回false
     */
    private boolean connect() {
        boolean isConnect = true;
        try {
            telnet.connect(ip, Integer.parseInt(port));
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            /** Log the user on* */
            readUntil("login: ");
            write(user);
            readUntil("password: ");
            write(password);
            /** Advance to a prompt */
            readUntil(DEFAULT_AIX_PROMPT);
        } catch (Exception e) {
            isConnect = false;
            e.printStackTrace();
            return isConnect;
        }
        return isConnect;
    }

    public void su(String user, String password) {
        try {
            write("su" + " - " + user);
            readUntil("Password:");
            write(password);
            readUntil(DEFAULT_AIX_PROMPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        // 处理编码，界面显示乱码问题
                        byte[] temp = sb.toString().getBytes("iso8859-1");
                        return new String(temp, "GBK");
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendCommand(String command) {
        try {
            write(command);
            return readUntil(DEFAULT_AIX_PROMPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getIPConfig() {
        this.connect();
        String result = this.sendCommand("ipconfig");
        this.disconnect();
        // 去除命令提示符
        return result.substring(0, result.indexOf(DEFAULT_AIX_PROMPT));
    }

    private String getDir() {
        this.connect();
        String result = this.sendCommand("dir");
        this.disconnect();
        // 去除命令提示符
        return result.substring(0, result.indexOf(DEFAULT_AIX_PROMPT));
    }

    public static void main(String[] args) {
        TelnetUtil telnet = new TelnetUtil("127.0.0.1", "administrator", "1");
        System.out.println(telnet.getDir());
        System.out.println(telnet.getIPConfig());
    }

}