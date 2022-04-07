package com.manageplat.service.sync;


import com.manageplat.service.sync.shell.PKeyShellConfig;

/**
 * 同步数据库设置基类
 *
 */
public class PKeyDBShellConfig extends PKeyShellConfig {

    private String mysqlDir = "mysql";

    private String dbHost;

    private String dbPassword;

    private String dbUser;

    private int dbPort = 3306;

    private String dbName;

    /**
	 * 获取mysql执行命令
	 */
	public String getMysqlDir() {
		return mysqlDir;
	}

    /**
	 * 设置mysql执行命令，系统环境没有时，则设置为mysql的安装目录/bin/mysql,否则默认mysql
	 */
	public void setMysqlDir(String mysqlDir) {
		this.mysqlDir = mysqlDir;
	}

    /**
	 * 获取访问数据库的用户
	 */
	public String getDbUser() {
		return dbUser;
	}

    /**
	 * 设置访问数据库的用户
	 */
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

    /**
	 * 获取要访问的数据库Host
	 */
	public String getDbHost() {
		return dbHost;
	}

    /**
	 * 设置要访问的数据库host
	 */
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

    /**
	 * 获取访问的数据库的密码
	 */
	public String getDbPassword() {
		return dbPassword;
	}

    /**
	 * 设置要访问的数据库的密码
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

    /**
	 * 获取访问的数据库端口
	 */
	public int getDbPort() {
		return dbPort;
	}

    /**
	 * 设置要访问的数据库端口
	 */
	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}

    /**
	 * 获取操作的数据库名称
	 */
	public String getDbName() {
		return dbName;
	}

    /**
	 * 设置操作的数据名称
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
}
