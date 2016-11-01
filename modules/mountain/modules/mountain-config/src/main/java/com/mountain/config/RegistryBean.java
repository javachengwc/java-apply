package com.mountain.config;

import com.mountain.model.SpecUrl;
import com.mountain.registry.Registry;
import com.mountain.registry.ZookeeperRegistry;
import com.util.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 注册节点
 */
public class RegistryBean  implements Serializable,InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(RegistryBean.class);

    //注册中心map id-->url
    public static ConcurrentMap<String,Registry> zookeeperRegistryMap = new ConcurrentHashMap<String,Registry>();

    //注册中心id(只是个标记)
    private String id;

    // 注册中心地址
    private String address;

    // 注册中心协议
    private String protocol = "zookeeper";

    // 注册中心请求超时时间(毫秒)
    private Integer timeout = 30000;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        if(NumberUtil.isNumeric(timeout)){
            this.timeout = Integer.parseInt(timeout);
        }
    }

    public static ConcurrentMap<String, Registry> getZookeeperRegistryMap() {
        return zookeeperRegistryMap;
    }

    public void init() {

        logger.info("RegistryBean init start............");
        if(StringUtils.isBlank(getAddress())){
            logger.error("RegistryBean init  address is null,不会启用注册中心!");
        }else {
            String urlStr=formatRegistryUrl(getAddress());
            SpecUrl url =SpecUrl.valueOf(urlStr);
            logger.info("RegistryBean gen url=\r\n"+url);
            Registry oldRegistry =zookeeperRegistryMap.putIfAbsent(getId(), new ZookeeperRegistry(url));
            if(oldRegistry==null)
            {
                logger.info("RegistryBean  create ZookeeperRegistry 创建并缓存注册中心成功, id-->"+getId()+",url="+urlStr);
            }else
            {
                logger.info("RegistryBean create ZookeeperRegistry 之前已创建并缓存此注册中心, id-->"+getId()+",url="+urlStr);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }

    //格式化zookeeper集群地址
    private String formatRegistryUrl(String address){

        StringBuilder sb = new StringBuilder("zookeeper://");
        if(StringUtils.isBlank(address))
        {
            return sb.toString();
        }
        String[] addressList = address.split(",");
        sb.append(addressList[0]);
        sb.append("?timeout=").append(this.getTimeout());
        if(addressList.length>1){
            sb.append("&backup=");
            for(int i=1;i<addressList.length;i++){
                if(i==1){
                    sb.append(addressList[i]);
                }else{
                    sb.append(",").append(addressList[i]);
                }
            }
        }
        return sb.toString();
    }

}
