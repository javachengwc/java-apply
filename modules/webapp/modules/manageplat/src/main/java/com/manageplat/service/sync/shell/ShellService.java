/**
 * 
 */
package com.manageplat.service.sync.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.manageplat.service.sync.ssh.SSHService;

/**
 * Sftp服务基础实现类
 *
 */
public abstract class ShellService extends SSHService implements IShellService {
	private Channel shell = null;
	private OutputStream out ;
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
