/**
 * 
 */
package com.manageplat.service.sync;

import com.manageplat.service.sync.sftp.ISftpService;
import com.manageplat.service.sync.shell.IShellService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 同步服务基类
 */
@SuppressWarnings("unchecked")
public abstract class SyncService implements ISyncService {

    protected final static Logger syncLogger = LoggerFactory.getLogger("syncLog.log");

	private IShellService shellService;

	private ISftpService uploadService;

	private ISftpService downloadService;

    private List<Object> dataList;

	public ISftpService getUploadService() {
		return uploadService;
	}

	public void setUploadService(ISftpService uploadService) {
		this.uploadService = uploadService;
	}

	public ISftpService getDownloadService() {
		return downloadService;
	}

	public void setDownloadService(ISftpService downloadService) {
		this.downloadService = downloadService;
	}

	public IShellService getDbService() {
		return shellService;
	}

	public void setShellService(IShellService shellService) {
		this.shellService = shellService;
	}

	@Override
	public void sync(String ids) {

        dataList=generateData(ids);

		try{
			doDownload();
			doUpload();
			doDbSync();
		}catch(Exception ex){
			ex.printStackTrace();
			downloadService.logout();
			uploadService.logout();
			shellService.logout();
		}
	}

	private void doDownload(){
		syncLogger.info("Sync Download Start.......");
		if(downloadService!=null){

            String path = "/resources";

			downloadService.login();
			for(Object obj:dataList){
				String targetDir = path+"/"+obj.toString();
				syncLogger.info("Download  dir:"+targetDir);
				downloadService.downLoadDir(targetDir);
			}
			downloadService.logout();
		}
        syncLogger.info("Sync Download End.......");
	}
	
	private void doUpload(){
        syncLogger.info("Sync Upload Start.......");
		if(uploadService!=null){
			String path = "/resources";
			uploadService.login();
            //伪实现
			for(Object obj:dataList){
				String targetDir = path+"/"+obj.toString();
                syncLogger.info("Upload dir:"+targetDir);
				uploadService.uploadFile(targetDir);
			}

			uploadService.logout();
		}
        syncLogger.info("Sync Upload End.......");
	}

	private void doDbSync(){
		syncLogger.info("Sync DB start.......");
		if(shellService!=null){
			shellService.login();
            //伪实现
			StringBuffer insertStrBuf = new StringBuffer("INSERT INTO xx ...VALUES ");

			for(int i=0;i<dataList.size();i++){
				if(i>0){
                    insertStrBuf.append(",");
				}
			}
			if(dataList.size()>0){
				shellService.execute(getCmd(insertStrBuf.toString()));
			}
			shellService.logout();
		}
		syncLogger.info("Sync DB end.......");
	}

	private String getCmd(String sql){
		sql = sql.replaceAll("`", "\\\\`");
		if(shellService.getServerConfig() instanceof PwdDBShellConfig){
			PwdDBShellConfig config = shellService.getServerConfig();
			return config.getMysqlDir()+" --default-character-set=utf8 -u"+config.getDbUser()+" -p"+config.getDbPassword()+" -P"+config.getDbPort()+" -D"+config.getDbName()+" -e\""+sql+"\"";
		}else if(shellService.getServerConfig() instanceof PKeyDBShellConfig){
			PKeyDBShellConfig config = shellService.getServerConfig();
			return config.getMysqlDir()+" --default-character-set=utf8 -u"+config.getDbUser()+" -p"+config.getDbPassword()+" -P"+config.getDbPort()+" -D"+config.getDbName()+" -e\""+sql+"\"";
		}else{
			return null;
		}
	}

    /**
     * 产生具体数据，子类实现
     */
    public List<Object> generateData(String ids)
    {
        return new ArrayList<Object>();
    }
}
