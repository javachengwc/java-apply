/**
 * 
 */
package com.manageplat.service.sync.sftp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.manageplat.service.sync.ssh.DefaultUserInfo;

/**
 * Sftp使用明文密码登录实现服务类
 * 
 */
public class PwdSftpService extends SftpService {
	@Override
	public Session createSession(JSch jsch){
		Session session = null;
		try{
			session = jsch.getSession(getServerConfig().getUser(), getServerConfig().getHost(), getServerConfig().getPort());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return session;
	}
	
	@Override
	public UserInfo createUserInfo(){
		DefaultUserInfo userInfo = new DefaultUserInfo();
		userInfo.setTrust(true);
		userInfo.setPassword(getServerConfig().getPassword());
    	return userInfo;
	}
}
