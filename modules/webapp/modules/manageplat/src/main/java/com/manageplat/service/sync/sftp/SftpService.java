/**
 * 
 */
package com.manageplat.service.sync.sftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.manageplat.service.sync.ssh.SSHService;

/**
 * Sftp服务基础实现类，包括上传和下载
 *
 */
public abstract class SftpService extends SSHService implements ISftpService{
	private ChannelSftp channelSftp = null;
	public ChannelSftp getChannelSftp() {
		if(channelSftp==null)
			try{
				Channel channel = getSession().openChannel("sftp");
				channel.connect();
				channelSftp=(ChannelSftp)channel;
			}catch(Exception ex){
				ex.printStackTrace();
			}
		return channelSftp;
	}
	
	private void createRemoteDir(File file){
		if(file.isFile()){
			String strFilePath = file.getAbsolutePath();
			String strFileDir = strFilePath.substring(0,strFilePath.lastIndexOf(File.separator));
			doCreateRemoteDir(strFileDir);
		}else if(file.isDirectory()){
			doCreateRemoteDir(file.getAbsolutePath());
		}
	}
	private void doCreateRemoteDir(String strFileDir){
		StringBuffer sb = new StringBuffer();
		for(String temp:strFileDir.split("\\"+File.separator)){
			if(!"".equals(temp.trim())){
				if(sb.capacity()==0)
					sb.append(temp);
				else
					sb.append(File.separator).append(temp);
				try{
                    String path=sb.toString();
					File dir = new File(path);
                    if(!dir.exists())
                    {
                        dir.mkdirs();
                    }
					this.getChannelSftp().mkdir(sb.toString());
				}catch(Exception ex){
					//ex.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected void doLogout(){
		if(channelSftp!=null){
			try{
				channelSftp.getSession().disconnect();
			}catch(JSchException ex){}
			channelSftp.disconnect();
			channelSftp=null;
		}
	}
	
	@Override
	public void uploadFile(String file) {
		uploadFile(new File(file));
	}
	
	@Override
	public void uploadFile(File file) {
		createRemoteDir(file);
		if(file.isFile()){
			doUploadFile(file);				
		}else if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File tmpFile:files){
				if(tmpFile.isFile())
					doUploadFile(tmpFile);
				else
					uploadFile(tmpFile);
			}
		}	
	}
	
	private void doUploadFile(File file){
		String strFilePath = file.getAbsolutePath();
		String strFileDir = strFilePath.substring(0,strFilePath.lastIndexOf(File.separator));
		int mode=ChannelSftp.OVERWRITE;
		try{
			getChannelSftp().cd(strFileDir);
			getChannelSftp().put(strFilePath, file.getName(), null, mode);
			logger.info("Success upload file:"+strFileDir+File.separator+file.getName());
		}catch(SftpException e){
			e.printStackTrace();
		}
	}

	public void downLoadFile(String file){
		downLoadFile(new File(file));
	}

	public void downLoadFile(File file){
		String fileName = file.getName();
		String dir = file.getAbsolutePath().replaceAll(fileName, "");
		dir = dir.substring(0, dir.length()-1);
		doDownloadFile(dir,fileName);
	}
	
	@Override
	public void downLoadDir(String dir){
		downLoadDir(new File(dir));
	}

    @SuppressWarnings("unchecked")
	@Override
	public void downLoadDir(File dir){
		String strFilePath = dir.getAbsolutePath();
		try{
			Vector<LsEntry> serverFiles = getChannelSftp().ls(strFilePath);
			for(LsEntry tmpFile :serverFiles){
				if(tmpFile.getAttrs().getPermissionsString().startsWith("d") && !".".equals(tmpFile.getFilename()) && !"..".equals(tmpFile.getFilename())){
					//目录
					String newStrFilePath = strFilePath+File.separator+tmpFile.getFilename();
					downLoadDir(newStrFilePath);
				}else if(tmpFile.getAttrs().getPermissionsString().startsWith("-")){
					//文件
					doDownloadFile(strFilePath,tmpFile.getFilename());
				}
			}
		}catch(SftpException e){
			e.printStackTrace();
		}
	}
	
	private void doDownloadFile(String Dir,String fileName){
		//文件
		createLocalDir(Dir);
        String absFile = Dir+File.separator+fileName;
		Vector<byte[]> getFile = new Vector<byte[]>();
		try{
			InputStream in = getChannelSftp().get(absFile);
			int length = 0;
	        int totalLength = 0;
	        byte[] buffer = new byte[1024];
	        try{
		        while ((length = in.read(buffer)) > 0){
		            byte[] tmpBuffer = new byte[length];
		            System.arraycopy(buffer, 0, tmpBuffer, 0, length);
		            getFile.addElement(tmpBuffer);
		            totalLength = totalLength + length;
		        }
		        in.close();
		        in=null;
	        }catch(Exception ex){
	        	ex.printStackTrace();
	        }
	        byte[] result = new byte[totalLength];
	        int pos = 0;
	        for (int i = 0; i < getFile.size(); i ++){
	            byte[] tmpBuffer = (byte[])getFile.elementAt(i);
	            System.arraycopy(tmpBuffer, 0, result, pos, tmpBuffer.length);   
	            pos = pos + tmpBuffer.length;
	        }		
	        try{
	        	FileOutputStream fos = new FileOutputStream(new File(absFile));
	        	fos.write(result);
	        	fos.close();
	        	fos=null;
	        	logger.info("Success download file:"+absFile);
	        }catch(Exception ex){
	        	ex.printStackTrace();
	        }
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			getFile.clear();
		}
	}
	private void createLocalDir(String dir){
		File file = new File(dir);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	@Override
	protected void afterLogin(){
		getChannelSftp();
	}
	@Override
	protected void beforLogin() {}
}
