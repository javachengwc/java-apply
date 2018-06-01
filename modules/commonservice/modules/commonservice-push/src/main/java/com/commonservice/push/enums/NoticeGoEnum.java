package com.commonservice.push.enums;

public enum NoticeGoEnum {

    GO_APP("go_app","打开应用"),

    GO_URL("go_url","跳转到URL"),

    GO_ACTIVITY("go_activity","打开特定的activity"),

    GO_CUSTOM("go_custom","用户自定义内容");

    private String value;

    private String note;

    NoticeGoEnum(String value,String note) {
        this.value=value;
        this.note=note;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
