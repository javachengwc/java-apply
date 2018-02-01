package com.shop.order.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "icbc.b2c")
@PropertySource(value ="classpath:icbc.properties",ignoreResourceNotFound=true)
public class IcbcConfig {

    public static String inputCharset;
    public static String interfaceName ;
    public static String interfaceVersion ;
    public static String language;
    public static String verifyJoinFlag;
    public static String curType;
    public static String merID;
    public static String merAcct;
    public static String creditType;
    public static String notifyType;
    public static String resultType;
    public static String merReference;
    public static String merCustomIp;
    public static String goodsType;
    public static String merHint;
    public static String merURL;
    public static String merVAR;
    public static String userCrtPath;
    public static String userKeyPath;
    public static String password;
    public static String gatewayUrl;
    public static String hangSupportFlag;
    public static String hangTimeInterval;

    public static String queryServerJksPath;
    public static String queryServerJksPassword;
    public static String queryUserJksPath;
    public static String queryUserJksPassword;
    public static String querySslPort;
    public static String queryHost;
    public static String queryHosPort;
    public static String queryPostMethod;
    public static String queryAPIName;
    public static String queryAPIVersion;
    public static String queryMerReqData;

    public static String getInputCharset() {
        return inputCharset;
    }

    public static void setInputCharset(String inputCharset) {
        IcbcConfig.inputCharset = inputCharset;
    }

    public static String getInterfaceName() {
        return interfaceName;
    }

    public static void setInterfaceName(String interfaceName) {
        IcbcConfig.interfaceName = interfaceName;
    }

    public static String getInterfaceVersion() {
        return interfaceVersion;
    }

    public static void setInterfaceVersion(String interfaceVersion) {
        IcbcConfig.interfaceVersion = interfaceVersion;
    }

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String language) {
        IcbcConfig.language = language;
    }

    public static String getVerifyJoinFlag() {
        return verifyJoinFlag;
    }

    public static void setVerifyJoinFlag(String verifyJoinFlag) {
        IcbcConfig.verifyJoinFlag = verifyJoinFlag;
    }

    public static String getCurType() {
        return curType;
    }

    public static void setCurType(String curType) {
        IcbcConfig.curType = curType;
    }

    public static String getMerID() {
        return merID;
    }

    public static void setMerID(String merID) {
        IcbcConfig.merID = merID;
    }

    public static String getMerAcct() {
        return merAcct;
    }

    public static void setMerAcct(String merAcct) {
        IcbcConfig.merAcct = merAcct;
    }

    public static String getCreditType() {
        return creditType;
    }

    public static void setCreditType(String creditType) {
        IcbcConfig.creditType = creditType;
    }

    public static String getNotifyType() {
        return notifyType;
    }

    public static void setNotifyType(String notifyType) {
        IcbcConfig.notifyType = notifyType;
    }

    public static String getResultType() {
        return resultType;
    }

    public static void setResultType(String resultType) {
        IcbcConfig.resultType = resultType;
    }

    public static String getMerReference() {
        return merReference;
    }

    public static void setMerReference(String merReference) {
        IcbcConfig.merReference = merReference;
    }

    public static String getMerCustomIp() {
        return merCustomIp;
    }

    public static void setMerCustomIp(String merCustomIp) {
        IcbcConfig.merCustomIp = merCustomIp;
    }

    public static String getGoodsType() {
        return goodsType;
    }

    public static void setGoodsType(String goodsType) {
        IcbcConfig.goodsType = goodsType;
    }

    public static String getMerHint() {
        return merHint;
    }

    public static void setMerHint(String merHint) {
        IcbcConfig.merHint = merHint;
    }

    public static String getMerURL() {
        return merURL;
    }

    public static void setMerURL(String merURL) {
        IcbcConfig.merURL = merURL;
    }

    public static String getMerVAR() {
        return merVAR;
    }

    public static void setMerVAR(String merVAR) {
        IcbcConfig.merVAR = merVAR;
    }

    public static String getUserCrtPath() {
        return userCrtPath;
    }

    public static void setUserCrtPath(String userCrtPath) {
        IcbcConfig.userCrtPath = userCrtPath;
    }

    public static String getUserKeyPath() {
        return userKeyPath;
    }

    public static void setUserKeyPath(String userKeyPath) {
        IcbcConfig.userKeyPath = userKeyPath;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        IcbcConfig.password = password;
    }

    public static String getGatewayUrl() {
        return gatewayUrl;
    }

    public static void setGatewayUrl(String gatewayUrl) {
        IcbcConfig.gatewayUrl = gatewayUrl;
    }

    public static String getHangSupportFlag() {
        return hangSupportFlag;
    }

    public static void setHangSupportFlag(String hangSupportFlag) {
        IcbcConfig.hangSupportFlag = hangSupportFlag;
    }

    public static String getHangTimeInterval() {
        return hangTimeInterval;
    }

    public static void setHangTimeInterval(String hangTimeInterval) {
        IcbcConfig.hangTimeInterval = hangTimeInterval;
    }

    public static String getQueryServerJksPath() {
        return queryServerJksPath;
    }

    public static void setQueryServerJksPath(String queryServerJksPath) {
        IcbcConfig.queryServerJksPath = queryServerJksPath;
    }

    public static String getQueryServerJksPassword() {
        return queryServerJksPassword;
    }

    public static void setQueryServerJksPassword(String queryServerJksPassword) {
        IcbcConfig.queryServerJksPassword = queryServerJksPassword;
    }

    public static String getQueryUserJksPath() {
        return queryUserJksPath;
    }

    public static void setQueryUserJksPath(String queryUserJksPath) {
        IcbcConfig.queryUserJksPath = queryUserJksPath;
    }

    public static String getQueryUserJksPassword() {
        return queryUserJksPassword;
    }

    public static void setQueryUserJksPassword(String queryUserJksPassword) {
        IcbcConfig.queryUserJksPassword = queryUserJksPassword;
    }

    public static String getQuerySslPort() {
        return querySslPort;
    }

    public static void setQuerySslPort(String querySslPort) {
        IcbcConfig.querySslPort = querySslPort;
    }

    public static String getQueryHost() {
        return queryHost;
    }

    public static void setQueryHost(String queryHost) {
        IcbcConfig.queryHost = queryHost;
    }

    public static String getQueryHosPort() {
        return queryHosPort;
    }

    public static void setQueryHosPort(String queryHosPort) {
        IcbcConfig.queryHosPort = queryHosPort;
    }

    public static String getQueryPostMethod() {
        return queryPostMethod;
    }

    public static void setQueryPostMethod(String queryPostMethod) {
        IcbcConfig.queryPostMethod = queryPostMethod;
    }

    public static String getQueryAPIName() {
        return queryAPIName;
    }

    public static void setQueryAPIName(String queryAPIName) {
        IcbcConfig.queryAPIName = queryAPIName;
    }

    public static String getQueryAPIVersion() {
        return queryAPIVersion;
    }

    public static void setQueryAPIVersion(String queryAPIVersion) {
        IcbcConfig.queryAPIVersion = queryAPIVersion;
    }

    public static String getQueryMerReqData() {
        return queryMerReqData;
    }

    public static void setQueryMerReqData(String queryMerReqData) {
        IcbcConfig.queryMerReqData = queryMerReqData;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
