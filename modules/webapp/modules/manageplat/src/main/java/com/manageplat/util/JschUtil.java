package com.manageplat.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * jsch工具相
 */
public class JschUtil {

    public static void ssh(String ip,Integer port ,String user,String psw,
                           String privateKey ,String passphrase,String cmd) throws Exception{
        Session session = null;
        Channel channel = null;
        JSch jsch = new JSch();
        if (!StringUtils.isBlank(privateKey) ) {
            if (!StringUtils.isBlank(passphrase)) {
                //设置带口令的密钥
                jsch.addIdentity(privateKey, passphrase);
            } else {
                //设置不带口令的密钥
                jsch.addIdentity(privateKey);
            }
        }
        if(port==null || port <=0){
            session = jsch.getSession(user, ip);
        }else{
           //采用指定的端口连接服务器
            session = jsch.getSession(user, ip ,port);
        }
        if (session == null) {
            throw new Exception("session is null");
        }
        session.setPassword(psw);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);

        try {
            //创建sftp通信通道
            channel = (Channel) session.openChannel("shell");
            channel.connect(1000);

            //获取输入流和输出流
            InputStream instream = channel.getInputStream();
            OutputStream outstream = channel.getOutputStream();

           //执行的SHELL命令,用\n结尾，表示回车
            String shellCommand = cmd+" \n";
            System.out.println("cmd:"+shellCommand);
            outstream.write(shellCommand.getBytes());
            outstream.flush();

           //获取命令执行的结果
            while (instream.available() > 0) {
                byte[] data = new byte[instream.available()];
                int nLen = instream.read(data);

                if (nLen < 0) {
                    throw new Exception("network error.");
                }

               //转换输出结果并打印出来
                String temp = new String(data, 0, nLen,"iso8859-1");
                System.out.println("----------------:"+temp);
            }
            outstream.close();
            instream.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }

    public static void main(String args []) throws Exception
    {
        ssh("127.0.0.1",22,"root","root",null,null,"ls");
    }
}
