/**
 * 
 */
package com.manageplat.service.sync.ssh;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * SSH登录的会话设置信息类
 *
 */
public class DefaultUserInfo implements UserInfo, UIKeyboardInteractive {

	public DefaultUserInfo(){}
	private String passphrase;
	private String password;
	private boolean trust = false;
    public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassphrase(){
        return passphrase;
    }
    public String getPassword(){
        return password;
    }
    public void setTrust(boolean trust) {
		this.trust = trust;
	}
	public boolean promptPassphrase(String message){
        return true;
    }
    public boolean promptPassword(String message){
        return true;
    }
    public boolean promptYesNo(String message){
        return trust;
    }
    public void showMessage(String message){}
    public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
        return null;
    }
}
