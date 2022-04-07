package com.manageplat.service.sync.env.remote;

import com.manageplat.service.sync.shell.PwdShellService;
import org.apache.commons.net.telnet.TelnetClient;

/**
 * 某远程服务器shell服务类
 *
 */
public class RemoteShellService extends PwdShellService {
	@Override
	public void beforLogin() {
		final RemoteShellConfig config = (RemoteShellConfig)getServerConfig();
		final TelnetClient telnet = new TelnetClient();
		Thread thread = new Thread(new Runnable(){
			public void run(){
				try{
					logger.info("telnet "+config.getTelnetHost()+" "+config.getTelnetPort());
					telnet.connect(config.getTelnetHost(), config.getTelnetPort());
				}catch(Exception ex){
				}
			}
		});
		thread.start();
		try{
			Thread.sleep(2000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
