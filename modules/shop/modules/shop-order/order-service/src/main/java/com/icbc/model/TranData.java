package com.icbc.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class TranData {

    private String interfaceName;

    private String interfaceVersion;

    private String orderDate;

    private String verifyJoinFlag;

    private String language;

    private String curType;

    private String merID;

    private String creditType;

    private String notifyType;

    private String resultType;

    private String merReference;

    private String merCustomIp;

    private String goodsType;

    private String merCustomID;

    private String merCustomPhone;

    private String goodsAddress;

    private String merOrderRemark;

    private String merHint;

    private String remark1;

    private String remark2;

    private String merURL;

    private String merVAR;

    private List orderInfoVector;

    private String merAcct;

    private String hangSupportFlag;

    private String hangTimeInterval;

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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getVerifyJoinFlag() {
        return verifyJoinFlag;
    }

    public void setVerifyJoinFlag(String verifyJoinFlag) {
        this.verifyJoinFlag = verifyJoinFlag;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurType() {
        return curType;
    }

    public void setCurType(String curType) {
        this.curType = curType;
    }

    public String getMerID() {
        return merID;
    }

    public void setMerID(String merID) {
        this.merID = merID;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getMerReference() {
        return merReference;
    }

    public void setMerReference(String merReference) {
        this.merReference = merReference;
    }

    public String getMerCustomIp() {
        return merCustomIp;
    }

    public void setMerCustomIp(String merCustomIp) {
        this.merCustomIp = merCustomIp;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getMerCustomID() {
        return merCustomID;
    }

    public void setMerCustomID(String merCustomID) {
        this.merCustomID = merCustomID;
    }

    public String getMerCustomPhone() {
        return merCustomPhone;
    }

    public void setMerCustomPhone(String merCustomPhone) {
        this.merCustomPhone = merCustomPhone;
    }

    public String getGoodsAddress() {
        return goodsAddress;
    }

    public void setGoodsAddress(String goodsAddress) {
        this.goodsAddress = goodsAddress;
    }

    public String getMerOrderRemark() {
        return merOrderRemark;
    }

    public void setMerOrderRemark(String merOrderRemark) {
        this.merOrderRemark = merOrderRemark;
    }

    public String getMerHint() {
        return merHint;
    }

    public void setMerHint(String merHint) {
        this.merHint = merHint;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getMerURL() {
        return merURL;
    }

    public void setMerURL(String merURL) {
        this.merURL = merURL;
    }

    public String getMerVAR() {
        return merVAR;
    }

    public void setMerVAR(String merVAR) {
        this.merVAR = merVAR;
    }

    public List getOrderInfoVector() {
        return orderInfoVector;
    }

    public void setOrderInfoVector(List orderInfoVector) {
        this.orderInfoVector = orderInfoVector;
    }

    public String getMerAcct() {
        return merAcct;
    }

    public void setMerAcct(String merAcct) {
        this.merAcct = merAcct;
    }

    public String getHangSupportFlag() {
        return hangSupportFlag;
    }

    public void setHangSupportFlag(String hangSupportFlag) {
        this.hangSupportFlag = hangSupportFlag;
    }

    public String getHangTimeInterval() {
        return hangTimeInterval;
    }

    public void setHangTimeInterval(String hangTimeInterval) {
        this.hangTimeInterval = hangTimeInterval;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
