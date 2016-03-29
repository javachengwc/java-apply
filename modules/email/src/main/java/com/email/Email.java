package com.email;

import java.util.Vector;

public class Email {

    //定义发件人、收件人、SMTP服务器、用户名、密码、主题、内容等
    private String displayName;
    private String to;
    private String from;
    private String smtpServer;
    private String username;
    private String password;
    private String subject;
    private String content;
    private boolean ifAuth; //服务器是否要身份认证
    private String filename="";
    @SuppressWarnings("unchecked")
    private Vector file = new Vector(); //用于保存发送附件的文件名的集合

    /**
     * 设置SMTP服务器地址
     */
    public void setSmtpServer(String smtpServer){
        this.smtpServer=smtpServer;
    }

    /**
     * 设置发件人的地址
     */
    public void setFrom(String from){
        this.from=from;
    }
    /**
     * 设置显示的名称
     */
    public void setDisplayName(String displayName){
        this.displayName=displayName;
    }

    /**
     * 设置服务器是否需要身份认证
     */
    public void setIfAuth(boolean ifAuth){
        this.ifAuth=ifAuth;
    }

    /**
     * 设置E-mail用户名
     */
    public void setUserName(String username){
        this.username=username;
    }

    /**
     * 设置E-mail密码
     */
    public void setPassword(String password){
        this.password=password;
    }

    /**
     * 设置接收者
     */
    public void setTo(String to){
        this.to=to;
    }

    /**
     * 设置主题
     */
    public void setSubject(String subject){
        this.subject=subject;
    }

    /**
     * 设置主体内容
     */
    public void setContent(String content){
        this.content=content;
    }

    /**
     * 该方法用于收集附件名
     */
    @SuppressWarnings("unchecked")
    public void addAttachfile(String fname){
        file.addElement(fname);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public boolean isIfAuth() {
        return ifAuth;
    }

    public String getFilename() {
        return filename;
    }

    public Vector getFile() {
        return file;
    }

    public Email(){

    }

    /**
     * 初始化SMTP服务器地址、发送者E-mail地址、用户名、密码、接收者、主题、内容
     */
    public Email(String smtpServer,String from,String displayName,String username,String password,String to,String subject,String content){
        this.smtpServer=smtpServer;
        this.from=from;
        this.displayName=displayName;
        this.ifAuth=true;
        this.username=username;
        this.password=password;
        this.to=to;
        this.subject=subject;
        this.content=content;
    }

    /**
     * 初始化SMTP服务器地址、发送者E-mail地址、接收者、主题、内容
     */
    public Email(String smtpServer,String from,String displayName,String to,String subject,String content){
        this.smtpServer=smtpServer;
        this.from=from;
        this.displayName=displayName;
        this.ifAuth=false;
        this.to=to;
        this.subject=subject;
        this.content=content;
    }

}
