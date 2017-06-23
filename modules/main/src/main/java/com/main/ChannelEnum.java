package com.main;

public enum ChannelEnum {

    Pc("pc","pc端"),Mobile("mobile","手机端");

    private String dd;

    private String node;

    private ChannelEnum(String dd,String node)
    {
        this.dd=dd;
        this.node=node;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
}
