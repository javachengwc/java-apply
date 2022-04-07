/**
 * 
 */
package com.manageplat.service.sync.sftp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.manageplat.service.sync.ssh.DefaultUserInfo;
import com.manageplat.service.sync.ssh.PKeySSHConfig;

/**
 * Sftp 使用public/private key登录实现服务类
 * 
 */
public class PKeySftpService extends SftpService {

	@Override
	public UserInfo createUserInfo() {
		DefaultUserInfo userInfo = new DefaultUserInfo();
		userInfo.setTrust(true);
		userInfo.setPassphrase(getServerConfig().getPassword());
		return userInfo;
	}

	@Override
	public Session createSession(JSch jsch){
		PKeySSHConfig config = (PKeySSHConfig)getServerConfig();
		Session session = null;
		try{
			jsch.addIdentity(config.getKeyFile(),config.getPassword());
			session = jsch.getSession(getServerConfig().getUser(), getServerConfig().getHost(), getServerConfig().getPort());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return session;
	}
}
