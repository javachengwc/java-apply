package com.manageplat.service.sync.env.dev;

import java.util.Date;

import com.manageplat.service.sync.ISyncService;
import com.manageplat.service.sync.PKeyDBShellConfig;
import com.manageplat.service.sync.SyncServiceFactory;
import com.manageplat.service.sync.forward.IForwardService;
import com.manageplat.service.sync.forward.PKeyForwardConfig;
import com.manageplat.service.sync.shell.IShellService;
import com.util.date.SysDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbSyncService implements ISyncService {

	protected final static Logger logger = LoggerFactory.getLogger("dbRync.log");

	private IForwardService forwardSlaveService;

    private IForwardService forwardDbService;

    private IShellService shellService;

    public IForwardService getForwardSlaveService() {
        return forwardSlaveService;
    }

    public void setForwardSlaveService(IForwardService forwardSlaveService) {
        this.forwardSlaveService = forwardSlaveService;
    }

    public IForwardService getForwardDbService() {
        return forwardDbService;
    }

    public void setForwardDbService(IForwardService forwardDbService) {
        this.forwardDbService = forwardDbService;
    }

    public IShellService getShellService() {
        return shellService;
    }

    public void setShellService(IShellService shellService) {
        this.shellService = shellService;
    }

    public DbSyncService(){

		setForwardSlaveService(SyncServiceFactory.getSlaveForwardService());
		setForwardDbService(SyncServiceFactory.getDbForwardService());
		setShellService(SyncServiceFactory.getDbShellService());
	}
	public void sync(String productIds){

		String file= "/db/tables_"+ SysDateTime.getDate()+".sql";

		try{
            forwardSlaveService.login();
		}catch(RuntimeException ex){
			ex.printStackTrace();
            forwardSlaveService.logout();
			return;
		}catch(Exception ex){
			ex.printStackTrace();
            forwardSlaveService.logout();
			return;
		}
		try{
			shellService.login();
		}catch(RuntimeException ex){
			ex.printStackTrace();
			shellService.logout();
			return;
		}catch(Exception ex){
			ex.printStackTrace();
			shellService.logout();
			return;
		}
		try{
			export2DB(file);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
            forwardSlaveService.logout();
		}
		try{
            forwardDbService.login();
		}catch(RuntimeException ex){
			ex.printStackTrace();
            forwardDbService.logout();
			return;
		}catch(Exception ex){
			ex.printStackTrace();
            forwardDbService.logout();
			return;
		}
		try{
			import2DB(file);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			shellService.logout();
            forwardDbService.logout();
		}
	}
	
	public void export2DB(String file){
		PKeyForwardConfig forwardConfig = forwardSlaveService.getServerConfig();
        //待改进
        String user="";
        String password="";
        String database="";
        String table="";

		String cmd = "mysqldump -h127.0.0.1 -P"+forwardConfig.getLport()+
                     " --skip-lock-tables -u" +user+
                     " -p"+password+" "+database+table+">"+file;

		shellService.execute(cmd);
		try{
			Thread.sleep(60000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		forwardSlaveService.logout();
	}
	
	public void import2DB(String file){
		PKeyDBShellConfig shellConfig = shellService.getServerConfig();
		String cmd = shellConfig.getMysqlDir()+
                " -h127.0.0.1 " +
                " -P"+shellConfig.getDbPort()+
                " -u"+shellConfig.getDbUser()+
                " -D " +shellConfig.getDbName()+
                " -e\""+"source "+file+"\"";
		shellService.execute(cmd);
		try{
			Thread.sleep(60000*2);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
