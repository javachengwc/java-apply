package com.manageplat.service.sync;


import com.manageplat.service.sync.env.dev.DbForwardService;
import com.manageplat.service.sync.env.dev.SlaveDbForwardService;
import com.manageplat.service.sync.env.local.LocalSftpService;
import com.manageplat.service.sync.env.remote.RemoteSftpConfig;
import com.manageplat.service.sync.env.remote.RemoteSftpService;
import com.manageplat.service.sync.env.remote.RemoteShellConfig;
import com.manageplat.service.sync.env.remote.RemoteShellService;
import com.manageplat.service.sync.forward.IForwardService;
import com.manageplat.service.sync.forward.PKeyForwardConfig;
import com.manageplat.service.sync.forward.PwdForwardConfig;
import com.manageplat.service.sync.forward.PwdForwardService;
import com.manageplat.service.sync.sftp.ISftpService;
import com.manageplat.service.sync.sftp.PwdSftpConfig;
import com.manageplat.service.sync.shell.IShellService;
import com.manageplat.service.sync.shell.PKeyShelService;
import com.manageplat.service.sync.shell.PwdShellService;

public class SyncServiceFactory {

	/**
	 * 获取本机器的sftp服务
	 */
	public static ISftpService getLocalSftpService(){

		PwdSftpConfig downloadConfig = new PwdSftpConfig();
		downloadConfig.setHost("127.0.0.1");
		downloadConfig.setPassword("");
		downloadConfig.setUser("root");
		downloadConfig.setPort(22);
		LocalSftpService downloadService = new LocalSftpService();
		downloadService.setServerConfig(downloadConfig);
		return downloadService;
	}
	
	/**
	 * 获取某远程机器sftp服务
	 */
	public static ISftpService getRemoteSftpService(){
		RemoteSftpConfig sftpConfig = new RemoteSftpConfig();
		sftpConfig.setHost("100.100.100.100");
		sftpConfig.setPassword("");
		sftpConfig.setPort(222);
		sftpConfig.setUser("admin");
		sftpConfig.setTelnetHost(sftpConfig.getHost());
		sftpConfig.setTelnetPort(123);
		RemoteSftpService uploadService = new RemoteSftpService();
		uploadService.setServerConfig(sftpConfig);
		return uploadService;
	}

	/**
	 * 获取某远程机器shell服务
	 */
	public static IShellService getRemoteShellService(){
		RemoteShellConfig shellConfig = new RemoteShellConfig();
		shellConfig.setHost("100.100.100.100");
		shellConfig.setPassword("");
		shellConfig.setPort(111);
		shellConfig.setUser("admin");
		shellConfig.setTelnetHost("100.100.100.100");
		shellConfig.setTelnetPort(123);
		
		shellConfig.setDbHost("");
		shellConfig.setDbName("");
		shellConfig.setDbPassword("");
		shellConfig.setDbPort(3306);
		shellConfig.setDbUser("");
		RemoteShellService dbService = new RemoteShellService();
		dbService.setServerConfig(shellConfig);
		return dbService;
	}

	/**
	 * 获取本机 shell服务
	 */
	public static IShellService getLocalShellService(){
		PwdDBShellConfig shellConfig = new PwdDBShellConfig();
		shellConfig.setHost("");
		shellConfig.setPassword("");
		shellConfig.setPort(22);
		shellConfig.setUser("");
		
		shellConfig.setDbHost("");
		shellConfig.setDbName("");
		shellConfig.setDbPassword("");
		shellConfig.setDbPort(3306);
		shellConfig.setDbUser("");
		
		PwdShellService shellService = new PwdShellService();
		shellService.setServerConfig(shellConfig);
		return shellService;
	}
	

	public static IForwardService getDbForwardService(){
		PwdForwardConfig forwardConfig = new PwdForwardConfig();
		forwardConfig.setHost("");
		forwardConfig.setPassword("");
		forwardConfig.setPort(22);
		forwardConfig.setUser("");
		
		forwardConfig.setLhost("");
		forwardConfig.setLport(3201);
		forwardConfig.setRhost("");
		forwardConfig.setRport(3307);
		
		IForwardService forwardService = new DbForwardService();
		forwardService.setServerConfig(forwardConfig);
		return forwardService;
	}
	
	public static IForwardService getSlaveForwardService(){
		PKeyForwardConfig forwardConfig = new PKeyForwardConfig();
		forwardConfig.setHost("");
		forwardConfig.setPassword("");
		forwardConfig.setPort(932);
		forwardConfig.setUser("");
		forwardConfig.setKeyFile("");
		forwardConfig.setLhost("");
		forwardConfig.setLport(3200);
		forwardConfig.setRhost("");
		forwardConfig.setRport(3307);
		SlaveDbForwardService forwardService = new SlaveDbForwardService();
		forwardService.setServerConfig(forwardConfig);
		return forwardService;
	}

    public static IShellService getDbShellService(){
        PKeyDBShellConfig shellConfig = new PKeyDBShellConfig();
        shellConfig.setHost("");
        shellConfig.setPassword("");
        shellConfig.setPort(932);
        shellConfig.setUser("");
        shellConfig.setKeyFile("");

        shellConfig.setDbHost("");
        shellConfig.setDbName("");
        shellConfig.setDbPassword("");
        shellConfig.setDbPort(3201);
        shellConfig.setDbUser("");
        shellConfig.setMysqlDir("");

        PKeyShelService shellService = new PKeyShelService();
        shellService.setServerConfig(shellConfig);
        return shellService;
    }
}
