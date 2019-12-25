package com.other.shell.util;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

//只得执行完了才一次性返回所有的结果，不能实时输出结果
public class RmtShellExecutor
{
	private String hostIp;
	private String userName;
	private String password;
	
	private Connection connection;
	private String charSet = Charset.defaultCharset().toString();
	
	private static final int TIME_OUT = 1000 * 50 * 60;
	
	public RmtShellExecutor(String hostIp, String userName, String password){
		this.hostIp = hostIp;
		this.userName = userName;
		this.password = password;
	}
	
	private boolean login() throws IOException{
		connection = new Connection(hostIp);
		connection.connect();
		return connection.authenticateWithPassword(userName, password);
	}
	
	public int execute(String command) throws Exception{
		InputStream stdOut = null;
		InputStream stdErr = null;
		String outStr = "";
		String outErr = "";
		int ret = -1;
		
		try{
			if(login()){
				Session session = connection.openSession();
				session.execCommand(command);
				
				stdOut = new StreamGobbler(session.getStdout());
				outStr = processStream(stdOut, charSet);
				System.out.println(outStr);
				stdErr = new StreamGobbler(session.getStderr());
				outErr = processStream(stdErr, charSet);
			    System.out.println(outErr);
				session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
				
				ret = session.getExitStatus();
			}else{
				throw new Exception("login error on "+hostIp+ "server");
			}
		}finally{
			if(connection != null){
				connection.close();
			}
			IOUtils.closeQuietly(stdOut);
			IOUtils.closeQuietly(stdErr);
		}
		
		return ret;
	}
	
	/**
	 * 返回shell输出结果
	 * @param command
	 * @return
	 * @throws Exception
	 */
	public String executeWithResult(String command) throws Exception{
		InputStream stdOut = null;
//		InputStream stdErr = null;
		String outStr = "";
//		String outErr = "";
//		int ret = -1;
		
		try{
			if(login()){
				System.out.println("RmtShellExecutor executeWithResult login success...");
				Session session = connection.openSession();
				session.execCommand(command);

				//只得执行完了才一次性返回所有的结果，不能实时输出结果
				stdOut = new StreamGobbler(session.getStdout());
				outStr = processStream(stdOut, charSet);
				session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
				
				session.getExitStatus();
			}else{
				throw new Exception("login error on "+hostIp+ "server");
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally{
			if(connection != null){
				connection.close();
			}
			IOUtils.closeQuietly(stdOut);
//			IOUtils.closeQuietly(stdErr);
		}
		
		return outStr;
	}
	
	private String processStream(InputStream in, String charset) throws Exception{
		byte[] buf = new byte[1024];
		StringBuilder sb = new StringBuilder();
		while(in.read(buf) != -1){
			sb.append(new String(buf, charset));
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception{
		RmtShellExecutor rmtShellExecutor = new RmtShellExecutor("47.107.237.10", "root", "CHeng123!");
		System.out.println("RmtShellExecutor main start...........");
		String result=rmtShellExecutor.executeWithResult("/tmp/test.sh");
		System.out.println("RmtShellExecutor exe shell...........");
		String resultArrayStr[] = result.split(" ");
		System.out.println(resultArrayStr[0]);
	}
}
