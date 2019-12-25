/**
 * 
 */
package com.manageplat.service.sync.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.manageplat.service.sync.ssh.SSHService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * Sftp服务基础实现类
 *
 */
public abstract class ShellService extends SSHService implements IShellService {
	private Channel shell = null;
	//写入该流的所有数据都将发送到远程端。
	private OutputStream out ;
	//从远程端到达的所有数据都能从这个流中读取到
	private InputStream in;
	@Override		
	public Channel getShell() {
		if(shell==null){
			try{
				shell = (ChannelShell)getSession().openChannel("shell");
				shell.connect();
				out = shell.getOutputStream();
				in = shell.getInputStream();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return shell;
	}
	
	@Override
	protected void doLogout() {
		if(shell!=null){
//			try{
//				shell.getSession().disconnect();
//			}catch(JSchException ex){}
			try{
				out.flush();
				out.close();
				in.close();
			}catch(IOException ex){}
			shell.disconnect();
			shell = null;
		}
	}

	@Override
	public void execute(String command) {
		try{
			logger.info("execute cmd:"+command);
			out.write(command.getBytes(Charset.forName("UTF-8")));
			out.write("\r\n".getBytes());
			out.flush();

			//获取到执行的输出
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String msg = "";
			List<String> resultList = new ArrayList<String>();
			while ((msg = reader.readLine()) != null) {
				resultList.add(msg);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	protected void afterLogin(){
		getShell();
	}
	@Override
	protected void beforLogin() {}
}
