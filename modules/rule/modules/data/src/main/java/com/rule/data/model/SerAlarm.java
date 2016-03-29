package com.rule.data.model;

public class SerAlarm {

    private Integer logid;

    private String sname;

    private String msg;

    public SerAlarm(String sname, String msg) {
        this.sname = sname;
        this.msg = msg;
    }

    public Integer getLogid() {
        return logid;
    }

    public void setLogid(Integer logid) {
        this.logid = logid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    @Override
    public String toString() {
        return sname == null ? msg : sname + ": " + msg;
    }
}
