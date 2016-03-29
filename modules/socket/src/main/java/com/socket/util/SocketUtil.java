package com.socket.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class SocketUtil
{
    public String conn(String ip, int port, String str, int timeOut)
            throws UnknownHostException, IOException
    {
        Socket sock = new Socket(ip, port);
        sock.setSoTimeout(timeOut);
        OutputStream os = sock.getOutputStream();
        os.write(str.getBytes());
        os.flush();
        InputStream is = sock.getInputStream();

        int n = is.read();
        StringBuffer sb = new StringBuffer();
        while (n != -1) {
            sb.append((char)n);
            n = is.read();
        }
        os.close();
        is.close();
        sock.close();
        return sb.toString();
    }

    public byte[] conn(String ip, int port, byte[] bs, int timeOut) throws UnknownHostException, IOException {
        Socket sock = new Socket(ip, port);
        sock.setSoTimeout(timeOut);
        OutputStream os = sock.getOutputStream();
        os.write(bs);
        os.flush();
        InputStream is = sock.getInputStream();

        ByteBuffer bb = ByteBuffer.allocate(1);
        int n = is.read();
        while (n != -1) {
            bb.put((byte)n);
            n = is.read();
        }
        os.close();
        is.close();
        sock.close();
        return bb.array();
    }

    public static void main(String[] args) {

        String s="";
        try {
            String w = new SocketUtil().conn("111.13.100.92", 80, s, 20000);
            System.out.println(w);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}