package com.commonservice.push.enums;

public enum NoticeCastEnum {

    UNICAST("unicast","单播","单播"),

    LISTCAST("listcast","列播","不超过500个device_token"),

    FILECAST("filecast","文件播","多个device_token可通过文件形式批量发送"),

    BROADCAST("broadcast","广播","广播"),

    GROUPCAST("groupcast","组播","组播"),

    CUSTOMIZEDCAST("customizedcast","自定义播",
            "通过alias进行推送" +
            "- alias: 对单个或者多个alias进行推送" +
            "- file_id: 将alias存放到文件后，根据file_id来推送");

    private String value;

    private String name;

    private String note;

    NoticeCastEnum(String value, String name, String note) {
        this.value=value;
        this.name=name;
        this.note=note;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
