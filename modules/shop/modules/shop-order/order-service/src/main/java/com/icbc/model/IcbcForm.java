package com.icbc.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public class IcbcForm {

    private String interfaceName;

    private String interfaceVersion;

    private String tranData;

    private String merSignMsg;

    private String merCert;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public String getTranData() {
        return tranData;
    }

    public void setTranData(String tranData) {
        this.tranData = tranData;
    }

    public String getMerSignMsg() {
        return merSignMsg;
    }

    public void setMerSignMsg(String merSignMsg) {
        this.merSignMsg = merSignMsg;
    }

    public String getMerCert() {
        return merCert;
    }

    public void setMerCert(String merCert) {
        this.merCert = merCert;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
