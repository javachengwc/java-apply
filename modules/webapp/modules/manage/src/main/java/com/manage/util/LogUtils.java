package com.manage.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 日志处理工具
 * 
 */
public class LogUtils {
	
	
	public final static String SPLIT = "|";

	public final static String SPLIT_ESCAPE = "\\|";

	private static ThreadLocal<List<IBaseLog>> logsThreadLocal = new ThreadLocal<List<IBaseLog>>();

	//用来保存操作的唯一标识
	private static ThreadLocal<String> identityThreadLocal = new ThreadLocal<String>();

	public final static Log houseSaleTraceLog = LogFactory.getLog("housesale.trace.log");
    //user日志
	public final static Log userLog = LogFactory.getLog("user.log");

	// 角色日志
	public final static Log roleLog = LogFactory.getLog("role.log");
	
	public final static Log testLog=LogFactory.getLog("test.log");
	
	// demo日志
	public final static Log dwarfsLog = LogFactory.getLog("dwarfs.log");
	
	public final static Log fileLog = LogFactory.getLog("file.log");
	
	public static ThreadLocal<List<IBaseLog>> getLogsThreadLocal() {
		return logsThreadLocal;
	}
     
	public static void recordLog(IBaseLog log)
	{
		List<IBaseLog> list =getLogsThreadLocal().get();
		if(list==null)
			list =new ArrayList<IBaseLog>();
		list.add(log);
	}
	////////////////////////////////////////////类,接口定义//////////////////////////
	// //////////////////
	// 每一个日志记录,对应一个内部类
	/**
	 * 日志接口
	 */
	public interface IBaseLog {
		public void loginfo();
	}
	public static class TestLog implements IBaseLog
	{
		private final String info; 

		public TestLog(String info) {
			super();
			this.info=info;
		}
		public void loginfo() {
			testLog.info(info.toString());
		}
	}

	public static class FileAlterLog implements IBaseLog {
		private final String type;

		private final String op;

		private final String path;

		private final long length;

		public FileAlterLog(String type, String op, String path, long length) {
			this.type = type;
			this.op = op;
			this.path = path;
			this.length = length;
		}

		public void loginfo() {
			StringBuilder buffer = new StringBuilder(255);
			buffer.append(System.currentTimeMillis()).append(SPLIT);
			buffer.append(type).append(SPLIT);
			buffer.append(op).append(SPLIT);
			buffer.append(path).append(SPLIT);
			buffer.append(length);
			fileLog.info(buffer.toString());
		}

	}

	////////////////////////////////////////////方法定义////////////////////////////
	// ////////////////
	
	/**
	 * 文件变更日志
	 * 
	 * @param type
	 *            文件类型，例如：居民上传头像：portrait，自定义
	 * @param op
	 *            文件操作类型，C：创建，U：更新；D：删除
	 * @param path
	 *            文件全路径
	 * @param length
	 *            新文件大小，删除为负数
	 */
	public static void logFileAlter(String type, String op, String path, long length) {
		List<IBaseLog> list = getLogList();
		list.add(new FileAlterLog(type, op, path, length));
	}

	private static List<IBaseLog> getLogList() {
		List<IBaseLog> list = logsThreadLocal.get();
		if (list == null) {
			list = new ArrayList<IBaseLog>();
			logsThreadLocal.set(list);
		}
		return list;
	}
    ///////////////////////////////////////////方法定义////////////////////////////
	// ////////////////

	public static void logFileInfo(String type, String op, String path, long length) {
		new FileAlterLog(type, op, path, length).loginfo();
	} 
    ///////////////////////////////////////////方法定义////////////////////////////
	// ////////////////
	/**
	 * 用户相关日志
	 * 
	 * @param type
	 * @param infos
	 */
	public static void logUserInfo(String type, String infos) {
		if (type == null || "".equals(type.trim()))
			throw new IllegalArgumentException();
		Log theLog = LogFactory.getLog(type + ".log");
		if (theLog == null)
			throw new IllegalArgumentException();
		theLog.debug(infos);
	}

	/**
	 * 通用
	 * 
	 * @param type
	 * @param infos
	 */
	public static void logInfo(String type, String infos) {
		if (type == null || "".equals(type.trim()))
			throw new IllegalArgumentException();
		Log theLog = LogFactory.getLog(type + ".log");
		if (theLog == null)
			throw new IllegalArgumentException();
		theLog.debug(infos);
	}
}