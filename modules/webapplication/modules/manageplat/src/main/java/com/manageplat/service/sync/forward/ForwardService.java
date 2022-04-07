/**
 * 
 */
package com.manageplat.service.sync.forward;

import com.jcraft.jsch.JSchException;
import com.manageplat.service.sync.ssh.SSHService;

/**
 * Forward服务基础实现类,包括将远程服务影射到本地以及将本地影射到远程机器上
 *
 */
public abstract class ForwardService extends SSHService implements
		IForwardService {
	@Override
	protected void doLogout() {
		if(this.getSession()!=null){
			this.getSession().disconnect();
			this.setSession(null);
		}
	}

	@Override
	public void createPortForwardLocal() {
		try{
			if(getServerConfig() instanceof PKeyForwardConfig){
				PKeyForwardConfig config = (PKeyForwardConfig)getServerConfig();
				getSession().setPortForwardingL(config.getLhost(), config.getLport(), config.getRhost(), config.getRport());
				logger.info(config.getLhost()+":"+config.getLport()+"-->"+config.getRhost()+":"+config.getRport());
			}else if(getServerConfig() instanceof PwdForwardConfig){
				PwdForwardConfig config = (PwdForwardConfig)getServerConfig();
				getSession().setPortForwardingL(config.getLhost(), config.getLport(), config.getRhost(), config.getRport());
				logger.info(config.getLhost()+":"+config.getLport()+"-->"+config.getRhost()+":"+config.getRport());
			}		
		}catch(JSchException ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	public void createPortForwardRemote() {
		try{
			if(getServerConfig() instanceof PKeyForwardConfig){
				PKeyForwardConfig config = (PKeyForwardConfig)getServerConfig();
				getSession().setPortForwardingR(config.getRhost(), config.getRport(),config.getLhost(), config.getLport());
				logger.info(config.getRhost()+":"+config.getRport()+"-->"+config.getLhost()+""+config.getLport());
			}else if(getServerConfig() instanceof PwdForwardConfig){
				PwdForwardConfig config = (PwdForwardConfig)getServerConfig();
				getSession().setPortForwardingR(config.getRhost(), config.getRport(),config.getLhost(), config.getLport());
				logger.info(config.getRhost()+":"+config.getRport()+"-->"+config.getLhost()+""+config.getLport());
			}
		}catch(JSchException ex){
			ex.printStackTrace();
		}
	}
	
	@Override
	protected void afterLogin(){}
	@Override
	protected void beforLogin() {}
}
