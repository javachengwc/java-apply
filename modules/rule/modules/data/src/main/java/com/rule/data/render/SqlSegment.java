package com.rule.data.render;

public class SqlSegment {

    private String str;

    private boolean isRequiredPara;

    public SqlSegment(String str, boolean requiredPara) {

        this.str = str;

        this.isRequiredPara = requiredPara;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public boolean getIsRequiredPara() {
        return isRequiredPara;
    }

    public void setIsRequiredPara(boolean isRequiredPara) {
        this.isRequiredPara = isRequiredPara;
    }
}
