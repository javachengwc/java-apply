/**
 * 
 */
package com.manageplat.service.sync.sftp;

import java.io.File;

import com.jcraft.jsch.ChannelSftp;
import com.manageplat.service.sync.ssh.ISSHService;

/**
 * Sftp服务基础接口
 *
 */
public interface ISftpService extends ISSHService {

    /**
	 * 获取sftp会话对象
     *
	 */
	public ChannelSftp getChannelSftp();

    /**
	 * 上传文件/目录
	 * @param file
	 */
	public void uploadFile(File file);

    /**
	 * 上传文件/目录
	 * @param file
	 */
	public void uploadFile(String file);

    /**
	 * 下载目录
	 * @param file
	 */
	public void downLoadDir(String file);

    /**
	 * 下载目录
	 * @param file
	 */
	public void downLoadDir(File file);

    /**
	 * 下载文件
	 * @param file
	 */
	public void downLoadFile(String file);

    /**
	 * 下载文件
	 * @param file
	 */
	public void downLoadFile(File file);
}
