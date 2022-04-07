package com.manageplat.service.sync.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象的SSH服务类
 * In order for SSH2 tunneling to function correctly one must ensure that the  
 * following line is uncommented in /etc/ssh/sshd_config :  
 * --------------------------CUT-------------------------------  
 * # Change to yes to enable tunnelled clear text passwords  
 * PasswordAuthentication yes  
 * --------------------------CUT-------------------------------  
 * Otherwise the initiation of the tunnel will fail with  
 * "SSH Initialization failed, try again?  
 * com.jcraft.jsch.JSchException: Auth fail"
 *
 */
public abstract class SSHService implements ISSHService {

    protected static final Logger logger= LoggerFactory.getLogger(SSHService.class);

	/**
	 * SSH会话对象
	 */
	private Session session;
	
	/**
	 * SSH登录设置信息对象
	 */
	private ISSHConfig serverConfig;
	
	@SuppressWarnings("unchecked")
	public <T extends ISSHConfig> T getServerConfig() {
		return (T)serverConfig;
	}
	public <T extends ISSHConfig> void setServerConfig(T serverConfig) {
		this.serverConfig = serverConfig;
	}
	
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public void login(){
		setServerConfig(serverConfig);
		beforLogin();
		JSch jsch = new JSch();
        try {
        	logger.info("ssh login>"+serverConfig.getUser()+"@"+serverConfig.getHost()+":"+serverConfig.getPort());
            session = createSession(jsch);
            session.setUserInfo(createUserInfo());
            session.connect();
            afterLogin();
        } catch (Exception ex) {
        	session = null;
            logger.error("ssh login error,",ex);
            throw new RuntimeException("ssh login error");
        }
	}
	
	public void logout(){

        logger.info("ssh logout>"+serverConfig.getUser()+"@"+serverConfig.getHost()+":"+serverConfig.getPort());
		try{
			doLogout();
			if(session!=null){
				session.disconnect();
				session=null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * SSH登录前操作，一般用过telnet之类
	 */
	protected abstract void beforLogin();

    /**
	 * SSH登录后，对会话设置，例如设置端口影射等操作
	 */
	protected abstract void afterLogin();

    /**
	 * SSH创建会话
	 * @param jsch 创建会话的对象
	 */
	protected abstract Session createSession(JSch jsch);

    /**
	 * 创建会话信息对象
	 * @return
	 */
	protected abstract UserInfo createUserInfo();

    /**
	 * 发生在SSH会话退出前
	 */
	protected abstract void doLogout();	 
}
